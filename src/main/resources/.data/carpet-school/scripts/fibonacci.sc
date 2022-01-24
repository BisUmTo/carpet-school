import('school','genera_domanda_libera','_n_ep','_force_closing_screen');
import('utils','_show_block_actionbar','_shuffle','_show_block_title','_random_item_around');
import('countdown','_valid_time','_start_countdown','_stop_countdown','_countdown_string','_countup_string','_time');
import('streak','_add_streak','_get_streak');
import('totem','_give_random_totem', '_on_player_uses_item','_on_player_dies','_on_player_respawns','_on_player_rides','_on_player_interacts_with_entity','_on_player_swings_hand');
import('math','_euclidean');

if(player() && player()~'gamemode'=='survival',
    global_item = _random_item_around(player()):0-'minecraft:';
    _show_block_title(player(), global_item);
);

run('/carpet fillUpdates false');

_n_ep(11);
__on_tick() -> (
    player = player();
    if(player && player~'gamemode'=='survival' && global_item,
        if((text=_countdown_string())!='', color='red',
           (text=_countup_string(200))!='', color ='green',
           text=''; color='white'
        );
        _show_block_actionbar(player,global_item,text,color);
    );

    if(player && player~'gamemode'=='survival' && global_item && _valid_time() &&
        inventory_find(player,global_item) == null,
        global_item = null;
        _stop_countdown();

        in_dimension(player~'dimension',particle('barrier',pos(player)+[0,player~'eye_height',0]+player~'look',1,0.1,0.3));
        schedule(10, _(outer(player)) -> _domanda_fibonacci(player));

    );
    if(player && player~'gamemode'=='survival' && (_valid_time() && _time()<-200 || _time()>0 && !global_item),
        _start_countdown();
        global_item = in_dimension(player~'dimension',_random_item_around(player)):0-'minecraft:';
        _show_block_title(player, global_item);
    )
);

_fibonacci(n,m) -> (
    list = if(n>m,[m,n],[n,m]);
    loop(rand(3)+2,
        list += list:(-1)+list:(-2)
    );
    list
);

_domanda_fibonacci(player) -> (
    genera_domanda_libera(player,
        list = _fibonacci(floor(rand(10)),floor(rand(10)+1));
        index = floor(rand(length(list)));
        risposta = list:index;
        list:index = '?';
        'Completa la successione!\n\n' + join(', ',list),
        risposta,
        _(player, r) -> (
            particle('happy_villager', pos(player)+[0,player~'eye_height',0]+player~'look');
            print(player, format('#00ff00 Esattamente! Ecco a te un fantastico premio!'));
            _start_countdown();

            // STREAK
            _add_streak(true);
            if(_get_streak()>4,
                print(player('all'),format(str('#00ff00 %s ha risposto correttamente a '+_get_streak()+' domande consecutive!',player~'team'||player)));
            );

            // RICOMPENSA
            _give_random_totem(player);
        ),
        _(player, r, corretta) -> (
            particle('wax_on', pos(player)+[0,player~'eye_height',0]+player~'look');
            print(player, format('#ffdd00 Accidenti! La risposta corretta era '+corretta));
            _start_countdown();
            
            // STREAK
            _add_streak(false);
            
            // PENALITA'
            schedule(20, '_materializza', player);
        )
    );
);

_materializza(player) -> (
    pos = pos(player);
    for(range(-16,16),
        x=_;
        for(range(-16,16),
            y=_;
            for(range(-16,16),
                z=_;
                pos1 = pos+[x,y,z];
                r = rand(dist = _euclidean(pos, pos1));
                if(dist >2 && r<8,
                    schedule(1 *(dist + floor(rand(dist+1))), _(outer(pos1),outer(player))->(
                        if(!rand(3),
                            run(str('execute in %s as %s run script in fibonacci run place_item(\'%s\',%d,%d,%d,\'down\',true)',
                                player~'dimension',
                                player~'command_name',
                                rand(item_list()),
                                ...pos1
                            ))
                        )
                    ))
                )
            )
        )
    )
);


// TOTEMS
__on_player_uses_item(player, item_tuple, hand)->_on_player_uses_item(player, item_tuple, hand);
__on_player_dies(player)->_on_player_dies(player);
__on_player_respawns(player)->_on_player_respawns(player);
__on_player_rides(player, forward, strafe, jumping, sneaking)->_on_player_rides(player, forward, strafe, jumping, sneaking);
__on_player_interacts_with_entity(player, entity, hand)->_on_player_interacts_with_entity(player, entity, hand);
__on_player_swings_hand(player, hand)->_on_player_swings_hand(player, hand);
__on_close() -> (
    _force_closing_screen(player());
);