package cliffordha.wolf_additions.item;

public enum TextFormatting {
    BOLD("§l"),
    ITALIC("§o"),
    UNDERLINED("§n"),
    STRIKETHROUGH("§m"),
    ;

    private final String marker;

    TextFormatting(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return this.marker;
    }
}
