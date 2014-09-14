package lineage2.gameserver.vaesoli;

public enum RpLanguage {
	COMMON(""),
	DWARVEN(" *nain* "),
	ORCISH(" *orc* "),
	ELVEN(" *elfique* "),
	DROW(" *sombre* "),
	KAMAEL(" *kamael* ")
    ;

    private RpLanguage(final String didascalie) {
    	_didascalie = didascalie;
    }

    private final String _didascalie;

    @Override
    public String toString() {
        return _didascalie;
    }
}
