import('school','genera_domanda_libera','_n_ep','_force_closing_screen');
import('utils','_random_block_around','_show_block_actionbar','_shuffle','_show_block_title');
import('countdown','_valid_time','_start_countdown','_stop_countdown','_countdown_string','_countup_string','_time');
import('streak','_add_streak','_get_streak');
import('totem','_give_random_totem', '_on_player_uses_item','_on_player_dies','_on_player_respawns');
import('math','_euclidean');

if(player() && player()~'gamemode'!= 'creative',
    global_block = _random_block_around(pos(player()));
    _show_block_title(player(), global_block);
);

run('/carpet fillUpdates false');

_n_ep(10);
__on_tick() -> (
    player = player();
    if(player && player~'gamemode'!= 'creative' && global_block,
        if((text=_countdown_string())!='', color='red',
           (text=_countup_string(600))!='', color ='green',
           text=''; color='white'
        );
        _show_block_actionbar(player,global_block,text,color);
    );

    if(player && player~'gamemode'!= 'creative' && global_block && _valid_time() && (
            (global_looked_block=player~['trace',25,'liquids']) == global_block || 
            (global_looked_block=player~['trace',25,'blocks']) == global_block
        ),
        global_block = null;
        _stop_countdown();

        in_dimension(player~'dimension',particle('barrier',pos(global_looked_block)+0.5,5,0.5,0.3));
        schedule(10, _(outer(player)) -> _domanda_media(player));

    );
    if(player && player~'gamemode'!= 'creative' && (_valid_time() && _time()<-600 || _time()>0 && !global_block),
        _start_countdown();
        global_block = in_dimension(player~'dimension',_random_block_around(pos(player)));
        _show_block_title(player, global_block);
    )
);

_domanda_media(player) -> (
    genera_domanda_libera(player,
        list = map(range(floor(rand(7)+5)),floor(rand(100)));
        sum = reduce(list,_a+_,0);
        len = length(list);
        if(sum%len,
            len += 1;
            list += len-sum%len;
            sum += list:(-1);
        );
        media = sum/len;
        list = _shuffle(list);
        'Quanto vale la media dei seguenti numeri?\n\n' + _impagina_numeri(list),
        media,
        _(player, r) -> (
            particle('happy_villager', pos(player)+[0,player~'eye_height',0]+player~'look');
            print(player, format('#00ff00 Esattamente! Ecco a te un fantastico premio!'));
            _start_countdown();

            // STREAK
            _add_streak(true);
            if(_get_streak()>4,
                print(player('all'),format(str('#00ff00 %s ha risposto correttamente a '+_get_streak()+' domande consecutive!',player)));
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

_impagina_numeri(list) -> (
    righe = [];
    for(list,
        riga = if(_i%2, righe:floor(_i/2), '');
        loop(rand(8)+2, riga += ' ');
        riga += _;
        righe:floor(_i/2) = riga;
    );
    righe = _shuffle(righe);
    join('\n',righe);
);

_materializza(player) -> (
    pos = pos(global_looked_block);
    for(range(-16,16),
        x=_;
        for(range(-16,16),
            y=_;
            for(range(-16,16),
                z=_;
                pos1 = pos+[x,y,z];
                r = rand(dist = _euclidean(pos, pos1));
                if(r<10,
                    schedule(1 *(dist + floor(rand(dist+1))), _(outer(pos1),outer(player))->(
                        run(str('execute in %s positioned %d %d %d unless block ~ ~ ~ air unless block ~ ~ ~ cave_air run setblock ~ ~ ~ %s',
                            player~'dimension',
                            ...pos1,
                            global_looked_block
                        ));
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

__on_close() -> (
    _force_closing_screen(player());
);