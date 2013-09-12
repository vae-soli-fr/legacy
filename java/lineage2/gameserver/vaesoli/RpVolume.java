package lineage2.gameserver.vaesoli;

public enum RpVolume {
	DEFAULT(""),
	WHISPER(" *chuchote* "),
	SHOUT(" *crie* ")
    ;

    private RpVolume(final String didascalie) {
    	_didascalie = didascalie;
    }

    private final String _didascalie;

    @Override
    public String toString() {
        return _didascalie;
    }
}
