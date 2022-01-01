import('school','genera_domanda_libera','_n_ep');
import('utils','_r_enchant','_spreadplayers');
import('countdown','_valid_time','_start_countdown','_stop_countdown');
import('streak','_add_streak','_get_streak');

_n_ep(9);
global_terne = [[3,4,5],[5,12,13],[7,24,25],[8,15,17],[9,40,41],[11,60,61],[12,35,37],[13,84,85],[16,63,65],[20,21,29],[28,45,53],[33,56,65],[36,77,85],[39,80,89],[48,55,73],[65,72,97]];

__on_player_stops_sprinting(player)-> if(_valid_time(),
    genera_domanda_libera(player,
        (
            terna = copy(rand(global_terne));
            if(rand(2), terna = [terna:1,terna:0, terna:2]);
            ignota = floor(rand(3));
            risposta = str(terna:ignota);
            terna:ignota = '?';
            str('Completa la terna pitagorica!\n\n%s %s %s\n
                 \nHint: Il quadrato del terzo è la somma dei primi due quadrati.',...terna)
        ),
        risposta,
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
            loop(_get_streak(),
                slot = floor(rand(inventory_size(player)));
                item_tuple = inventory_get(player, slot);
                if(item_tuple,
                    item_tuple = _r_enchant(item_tuple);
                    [item, count, nbt] = item_tuple;
                    inventory_set(player, slot, count, item, nbt)
                )
            );           
        ),
        _(player, r, corretta) -> (
            particle('wax_on', pos(player)+[0,player~'eye_height',0]+player~'look');
            print(player, format('#ffdd00 Accidenti! La risposta corretta era '+corretta));
            _start_countdown();
            
            // STREAK
            _add_streak(false);
            
            // PENALITA'
            _spreadplayers(player);
        )
    );

    _stop_countdown();
);
