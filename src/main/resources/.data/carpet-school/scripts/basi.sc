__config() -> {
    'commands' -> {
        '<risp>' -> '_risposta',
        'skip' -> '_unfreeze',
        'random <bool>' -> _(random) -> global_random = random
    },
    'arguments' -> {
        'risp' -> {'type' -> 'int', 'min' -> 0, 'max' -> 2}
    }
};

// GLOBALI
global_time = 300;
global_countdown = world_time()-global_time;
global_domanda = 0;
global_n_risposta_corretta = null;
global_lettere = ['a)','b)','c)'];
global_bonus = null;
global_slot = null;
global_pos = null;
global_block = null;
global_random = true;

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
_modify_inventory(player, m) -> (
    for([range(inventory_size(player))],
        if(rand(100)< 4 * global_domanda,
            item_tuple = inventory_get(player, _i);
            if(item_tuple,
                [item, count, nbt] = item_tuple;
                count = min(ceil(count * (100+m*global_percentuale)/100),999999);
                if(nbt:'Damage', nbt:'Damage'+= -global_percentuale*m);
                inventory_set(player, _i, count, item, nbt)
            )
        )
    );
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

// DOMANDA RISPOSTA
global_primi = [1,2,3,5,7,11,13,17,19];
_domanda() -> (
    _freeze();
    print('=====================================================');
    print(format('b#ff0000 MATEMATICA CON MINECRAFT') +  ' #5.' + (global_domanda+=1));
    print(format('i Rispondi correttamente per ricevere un premio!\n'));

    base = floor(rand(8)+2);
    numero = floor(rand(100));
    numero_in_base = _base(numero,base);
    global_bonus = numero_in_base;
    if(rand(2), // ? to 10
        print(str('Il numero %d in base %d, quanto vale in base decimale?',
            numero_in_base,
            base
        ));
        global_risposta_corretta = numero;
        r1 = number(_base(abs(numero+floor(rand(number))-floor(rand(base))),base));
        r2 = floor(rand(3))+number;
    , // else: 10 to ?
        print(str('Il numero %d in base decimale, quanto vale in base %d?',
            numero,
            base
        ));
        global_risposta_corretta = numero_in_base;
        r1 = number(_base(abs(numero+floor(rand(number))-floor(rand(base))),base));
        r2 = number(_base(abs(numero+floor(rand(number))),min(10,base+floor(if(rand(2),1,-1)*rand(3)))));
    );

    if(r1 == global_risposta_corretta,
        r1 += floor(rand(10))
    );
    while(r1 == global_risposta_corretta || !r1, 127,
        r1 += if(rand(2),1,-1)*floor(rand(10))
    );
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
        print(format(' ' + global_lettere:_i, '!/basi '+_i)+' '+format(' ? = '+_,'!/basi '+_i)),
    );
    print('=====================================================');
    global_countdown = world_time();
);
_risposta(risp) -> (
    if(global_n_risposta_corretta != null,
        p = player();
        if(risp == global_n_risposta_corretta,
            // CORRETTA
            particle('happy_villager', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#00ff00 Esattamente! Ecco a te il tuo premio!'));
            if(global_slot != null && global_bonus != null,
                [item, count, nbt] = if(lista = inventory_get(p, global_slot), lista, [global_item:1, 0, null]);
                inventory_set(p, global_slot, count + number(global_bonus), item, nbt)
            );
        ,   // SBAGLIATA
            particle('wax_on', pos(p)+[0,p~'eye_height',0]+p~'look');
            print(format('#ffdd00 Accidenti! La risposta corretta era la '+global_lettere:global_n_risposta_corretta));
            if(global_slot != null && global_block_pos != null && global_block != null,
               _divora(p, global_block_pos)
            );
        );
        global_n_risposta_corretta = null;
    );
    _unfreeze();
);
_divora(player, pos) -> (
    item = inventory_get(player,global_slot);
    if(item:0 == global_item:1,
        block = first(
            if(global_random,_shuffle([diamond(pos,3,3)]),diamond(pos,3,3)),
            air(_) && _distance(pos(_),pos(player))>2 && rand(2)
        );
        if(block != null,
            pos = pos(block);
            particle('wax_on', pos);
            set(pos, global_block);
            inventory_set(player,global_slot, item:1 - 1, item:0, item:2);
            schedule(floor(rand(2)), '_divora', player, pos)
        )
    )
);

// FREEZE
global_pos = {};
run('carpet creativeFlySpeed 0');
_freeze() -> (
    if(run('tick freeze'):1:0~'normally',run('tick freeze'));
    for(player('all'),
        global_pos:_ = pos(_);
        fly_speed(_, 0);
        modify(_, 'motion', 0, 0, 0);
        modify(_, 'gamemode', 'spectator')
    )
);
_unfreeze() -> (
    if(run('tick freeze'):1:0~'frozen',run('tick freeze'));
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
__on_player_places_block(player, item_tuple, hand, block)-> (
    if(world_time()-global_countdown > global_time,
        global_item = [item_tuple:1, item_tuple:0];
        global_slot = player~'selected_slot';
        global_block_pos = pos(block);
        global_block = block;
        _domanda()
    );
);
