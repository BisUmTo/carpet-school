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
            desc = "...",
            //extra = {"Se si risponde correttamente, si otterrà un oggetto extra",
            //        "Se la risposta dovesse essere errata, si potrebbe perdere l'oggetto raccolto."},
            appSource = "frazioni",
            category = {SURVIVAL, SCHOOL}
    )
    public static boolean EP2_FRAZIONI = true;
}
