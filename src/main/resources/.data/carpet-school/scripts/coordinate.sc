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
global_countdown = system_info('world_time')-global_time;
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


// DOMANDA RISPOSTA
global_primi = [1,2,3,5,7,11,13,17,19];
_domanda() -> (
    _freeze();
    print('=====================================================');
    print(format('b#ff0000 MATEMATICA CON MINECRAFT') +  ' #8.' + (global_domanda+=1));
    print(format('i Rispondi correttamente per ricevere un premio!\n'));

    coordinate = map(pos(player()), round(_));
    offset = map(range(3), if(rand(2),1,-1)*floor(rand(26))); // lista di 3 numeri da -25 a 25
    

    es = floor(rand(5));
    if(es == 0,

    );

    r2 = floor(rand(100));
    while(r2 == global_risposta_corretta || r2 == r1 || !r2, 127,
        r2 += if(rand(2),1,-1)*floor(rand(10))
    );

    possibili_risposte = [global_risposta_corretta,r1];
    if(r2 != global_risposta_corretta && r2 != r1,
        possibili_risposte = [...possibili_risposte, r2]
    );
    risposte_disordinate = _c_shuffle(possibili_risposte);

    global_n_risposta_corretta = risposte_disordinate~global_risposta_corretta;

    for(risposte_disordinate,
        print(format(' ' + global_lettere:_i, '!/pigreco '+_i)+' '+format(' ? = '+_,'!/pigreco '+_i)),
    );
    print('=====================================================');
    global_countdown = system_info('world_time')
;
);
_risposta(risp) -> (
    if(global_n_risposta_corretta != null,
        p = player();
        if(risp == global_n_risposta_corretta,
            // CORRETTA
            particle('happy_villager', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#00ff00 Esattamente! Ecco a te il tuo premio!'));

            if(inventory_size(global_chest) > 0,
                run(str('loot insert %d %d %d loot chests/%s',[
                    ...pos(global_chest),
                    rand(global_chests)
                ])),
                run(str('loot spawn %f %f %f loot chests/%s',[
                    ...(pos(global_chest)+[0.5,1,0.5]),
                    rand(global_chests)
                ]))
            );

        ,   // SBAGLIATA
            particle('wax_on', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#ffdd00 Accidenti! La risposta corretta era la '+global_lettere:global_n_risposta_corretta));

            run(str('execute as %s at @s in minecraft:the_nether run tp @s ~ ~ ~', p));
            run(str('execute as %s at @s run spreadplayers ~ ~ 10 100 under 100 false @s', p));
            global_pos:p = pos(p);
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
global_blocchi = ['smoker','cartography_table','crafting_table','chest','furnace','blast_furnace','barrel'];
__on_player_interacts_with_block(player, hand, block, face, hitvec) -> (
    if(system_info('world_time')-global_countdown > global_time && global_blocchi~block != null,
        global_chest = block;
        _domanda()
    )
)
