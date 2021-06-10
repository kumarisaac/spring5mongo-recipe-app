package guru.springframework.domain;

public enum Difficulty {
    EASY("Easy"), MODERATE("Moderate"), HARD("Hard");

    private String text;

    private Difficulty(String text){
        this.text = text;
    }

    public String getText(){
        return this.text;
    }
}

