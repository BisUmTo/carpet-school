package it.bisumto.carpetschool;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.SURVIVAL;

public class CarpetSchoolRules {
    private static final String SCHOOL = "school";
    @Rule(
            desc = "Quando si raccoglie un oggetto da terra, verrà posto un quesito matematico.",
            extra = {"Se si risponde correttamente, si otterrà un oggetto extra",
                     "Se la risposta dovesse essere errata, si potrebbe perdere l'oggetto raccolto."},
            appSource = "monomi",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP1_MONOMI = false;

    @Rule(
            desc = "Quando si crafta un oggetto, verrà mostrata una proporzione con 1 incognita.",
            extra = {"Se si risponde correttamente, raddoppiavi il risultato.",
                     "Se la risposta dovesse essere errata, si perderebbe il risultato."},
            appSource = "proporzioni",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP2_PROPORZIONI = false;

    @Rule(
            desc = "Quando si cambia slot dell'inventario, viene mostrato un semplice modulo.",
            extra = {"Se si risponde correttamente, si incanta l'oggetto con un incantesimo a caso.",
                    "Se la risposta dovesse essere errata, si disordinerebbe l'inventario."},
            appSource = "modulo",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP3_MODULO = false;

    @Rule(
            desc = "Quando si utilizza un utensile, viene mostrato una percentuali da risolvere.",
            extra = {"Se si risponde correttamente, si ottiene una % di un sottoinsieme di oggetti dell'inventario.",
                    "Se la risposta dovesse essere errata, si perde una % di un sottoinsieme di oggetti dell'inventario."},
            appSource = "percentuali",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP4_PERCENTUALI = true;

    @Rule(
            desc = "Quando si piazza un blocco, viene mostrato un esercizio di cambio basi da risolvere.",
            extra = {"Se si risponde correttamente, si aggiungono dei blocchi allo stack corrente.",
                    "Se la risposta dovesse essere errata, si piazzeranno tutti i blocchi dello stack corrente."},
            appSource = "basi",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP5_BASI = false;

    @Rule(
            desc = "Quando un giocatore prende danno, viene mostrata un'operazione tra fattoriali.",
            extra = {"Se si risponde correttamente, si guadagneranno cuori pari al danno inflitto",
                    "Se la risposta dovesse essere errata, si perderanno altrettanti cuori."},
            appSource = "fattoriale",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP6_FATTORIALE = false;

    @Rule(
            desc = "Quando un giocatore apre una cassa, viene mostrata un'operazione con pi greco.",
            extra = {"Se si risponde correttamente, droppa un'armatura \"numerata\"",
                    "Se la risposta dovesse essere errata, ti teletrasporta in una nuova dimensione."},
            appSource = "pigreco",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP7_PIGRECO = true;
}
