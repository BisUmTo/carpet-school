__config() -> {
    'commands' -> {
        '<risp>' -> '_risposta',
        'skip' -> '_unfreeze',
        'decimali <bool>' -> _(bool) -> global_decimali = bool
    },
    'arguments' -> {
        'risp' -> {'type' -> 'int', 'min' -> 0, 'max' -> 2}
    }
};

// GLOBALI
global_time = 300;
global_countdown = system_info('world_time');
global_domanda = 0;
global_n_risposta_corretta = null;
global_lettere = ['a)','b)','c)'];
global_decimali = false;
global_chest = null;
global_chests = ['abandoned_mineshaft','bastion_bridge','bastion_hoglin_stable','bastion_other','bastion_treasure','buried_treasure','desert_pyramid','end_city_treasure','igloo_chest','jungle_temple','jungle_temple_dispenser','nether_bridge','pillager_outpost','ruined_portal','shipwreck_map','shipwreck_supply','shipwreck_treasure','simple_dungeon','spawn_bonus_chest','stronghold_corridor','stronghold_crossing','stronghold_library','underwater_ruin_big','underwater_ruin_small','village/village_armorer','village/village_butcher','village/village_cartographer','village/village_desert_house','village/village_fisher','village/village_fletcher','village/village_mason','village/village_plains_house','village/village_savanna_house','village/village_shepherd','village/village_snowy_house','village/village_taiga_house','village/village_tannery','village/village_temple','village/village_toolsmith','village/village_weaponsmith','woodland_mansion'];

// UTILS
_mcd(a,b) -> (
    while(b != 0, 127,
        [a, b] = [b, a % b];
    );
    a
);
_mcm(num1,num2) -> num1*num2/_mcd(num1,num2);
_remove_match(l, match) -> (
    t = [];
    for(l,
        if(_~match == null, t += _)
    );
    t
);
_shuffle(l) -> (
    loop((s=length(l))-1,
        j = rand(s-_)+_;
        t = l:_;
        l:_ = l:j;
        l:j = t
    );
    l
);
_c_shuffle(list) -> (
    l = copy(list);
    _shuffle(l)
);
_inventory_list(inv) -> map([range(inventory_size(inv))],
    inventory_get(inv, _i)
);
_n_inventory_list(inv) -> (
    il = [];
    for(_inventory_list(inv), if(_, il += _));
    il
);
global_cifre = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'];
_base(n,b) -> (
    c = if(n==0 || b == 0,'0','');
    while(n>0 && b > 0, n,
        c = global_cifre:(n%b) + c;
        n = floor(n/b);
    );
    c
);
_distance(v1,v2) -> sqrt(reduce(v1 - v2, _a + _*_, 0));
_attribute(entity, attribute, ...variables) -> (
    if(length(variables) == 0,
        run(str('attribute %s %s base get',
            entity~'command_name',
            attribute
        )):0;
    , length(variables) == 1,
        run(str('attribute %s %s base set %d',
            entity~'command_name',
            attribute,
            variables:0
        )):0;
    , null
    )
);
_get_max_health(player) -> _attribute(player, 'generic.max_health');
_set_max_health(player, amount) -> _attribute(player, 'generic.max_health', amount);
_add_max_health(player, amount) -> _attribute(player, 'generic.max_health', max(1,_attribute(player, 'generic.max_health') + amount));
_coord(coordinate) -> (
    'X: '+coordinate:0+
    ', Y: '+coordinate:1+
    ', Z: '+coordinate:2
);

// DOMANDA RISPOSTA
global_primi = [1,2,3,5,7,11,13,17,19];
_domanda() -> (
    _freeze();
    print(player(),'=====================================================');
    print(player(),format('b#ff0000 MATEMATICA CON MINECRAFT') +  ' #8.' + (global_domanda+=1));
    print(player(),format('i Rispondi correttamente per ricevere un premio!\n'));

    coordinate = map(pos(player()), round(_));
    offset = map(range(3), if(rand(2),1,-1)*floor(rand(26))); // lista di 3 numeri da -25 a 25
    coordinate_spostate = coordinate + offset;

    es = floor(rand(3));
    if(es == 0,
        print(player(),format(
            ' Ti trovi a coordinate ',
            str('b %s\n',_coord(coordinate)),
            ' Se percorri ',
            str('b %d',offset:0),
            '  blocchi sull\'asse delle ', 'b X', ' , ',
            str('b %d', offset:1),
            '  sull\'asse delle ', 'b Y', '  e ',
            str('b %d', offset:2),
            '  sull\'asse delle ', 'b Z', ' \n',
            ' A che coordinate ti troverai?'
        ));
        global_risposta_corretta = coordinate_spostate;
        r1 = coordinate - offset;
        while(r1 == global_risposta_corretta, 127,
            r1:floor(rand(3)) += if(rand(2),1,-1)*floor(rand(10))
        );
        r2 = coordinate + offset;
        while(r2 == global_risposta_corretta || r2 == r1, 127,
            r2:floor(rand(3)) += if(rand(2),1,-1)*floor(rand(10))
        );
        global_risposta_corretta = _coord(global_risposta_corretta);
        r1 = _coord(r1);
        r2 = _coord(r2);
    , es == 1,
        print(player(),format(
            ' Ti trovi a coordinate ',
            str('b %s\n',_coord(coordinate)),
            ' Se vuoi arrivare a coordinate ',
            str('b %s',_coord(coordinate_spostate))
        ));
        sottes = floor(rand(3));
        if(sottes == 0,
            print(player(),'Quanto ti devi muovere sull\'asse X?');
            global_risposta_corretta = offset:0;
            r2 = if(rand(2),1,-1)*offset:if(rand(2),1,2);
        , sottes == 1,
            print(player(),'Quanto ti devi muovere sull\'asse Y?');
            global_risposta_corretta = offset:1;
            r2 = if(rand(2),1,-1)*offset:if(rand(2),0,2);
        , sottes == 2,
            print(player(),'Quanto ti devi muovere sull\'asse Z?');
            global_risposta_corretta = offset:2;
            r2 = if(rand(2),1,-1)*offset:if(rand(2),0,1);
        );
        r1 = if(rand(2), -global_risposta_corretta, -r2);
        while(r1 == global_risposta_corretta, 127,
            r1 += if(rand(2),1,-1)*floor(rand(10))
        );
        while(r2 == global_risposta_corretta || r2 == r1, 127,
            r2 += if(rand(2),1,-1)*floor(rand(10))
        );
    , es == 2,
        print(player(),format(
            ' Ti trovi a coordinate ',
            str('b %s\n',_coord(coordinate_spostate)),
            ' Se ti trovavi a coordinate ',
            str('b %s\n',_coord(coordinate)),
            ' Di quanto ti sei spostato?'
        ));
        global_risposta_corretta = offset;
        r1 = map(range(3), if(rand(2),1,-1)*floor(rand(26)));
        while(r1 == global_risposta_corretta, 127,
            r1:floor(rand(3)) += if(rand(2),1,-1)*floor(rand(10))
        );
        r2 = map(range(3), if(rand(2),1,-1)*floor(rand(26)));
        while(r2 == global_risposta_corretta || r2 == r1, 127,
            r2:floor(rand(3)) += if(rand(2),1,-1)*floor(rand(10))
        );
        global_risposta_corretta = _coord(global_risposta_corretta);
        r1 = _coord(r1);
        r2 = _coord(r2);
    );

    possibili_risposte = [global_risposta_corretta];
    if(r1 != global_risposta_corretta,
        possibili_risposte = [...possibili_risposte, r1]
    );
    if(r2 != global_risposta_corretta && r2 != r1,
        possibili_risposte = [...possibili_risposte, r2]
    );
    risposte_disordinate = _c_shuffle(possibili_risposte);

    global_n_risposta_corretta = risposte_disordinate~global_risposta_corretta;

    for(risposte_disordinate,
        print(player(),format(' ' + global_lettere:_i, '!/coordinate '+_i)+' '+format(' '+_,'!/coordinate '+_i)),
    );
    print(player(),'=====================================================');
    global_countdown = system_info('world_time')
;
);
_risposta(risp) -> (
    if(global_n_risposta_corretta != null,
        p = player();
        if(risp == global_n_risposta_corretta,
            // CORRETTA
            particle('happy_villager', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#00ff00 Esattamente! I controlli sono stati ripristinati!'));
            run('carpet EP8_COORDINATE_DIR 0');
            modify(p,'effect','speed',global_time+if(rand(2),1,-1)*floor(rand(300)),rand(10))
        ,   // SBAGLIATA
            particle('wax_on', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#ffdd00 Accidenti! La risposta corretta era la '+global_lettere:global_n_risposta_corretta));
            dir = floor(rand(3)+1);
            run('carpet EP8_COORDINATE_DIR '+dir);
            if(dir == 1,
                 print(format('#ff0000 Controlli ruotati in senso orario.'));
            , dir == 2,
                 print(format('#ff0000 Controlli invertiti.'));
            , dir == 3,
                 print(format('#ff0000 Controlli ruotati in senso anti-orario.'));
            )

        );
        global_n_risposta_corretta = null;
    );
    _unfreeze();
);

// FREEZE
global_pos = {};
_freeze() -> (
    run('carpet creativeFlySpeed 0');
    run('tick freeze on');
    for(player('all'),
        global_pos:_ = pos(_);
        fly_speed(_, 0);
        modify(_, 'motion', 0, 0, 0);
        modify(_, 'gamemode', 'spectator')
    )
);
_unfreeze() -> (
    run('carpet creativeFlySpeed 1');
    run('tick freeze off');
    for(player('all'),
        modify(_, 'gamemode', 'survival');
        if(global_pos:_,
            modify(_, 'pos', global_pos:_)
        );
        fly_speed(_, 0.03);
        delete(global_pos:_)
    );
);
_unfreeze();

// EVENTI
__on_tick() -> (
    if(system_info('world_time')-global_countdown > global_time,
        global_time = floor(rand(60)+30)*20;
        _domanda();
    )
)
