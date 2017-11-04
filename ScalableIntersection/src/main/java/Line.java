import java.util.Arrays;

public class Line {

    Character letter;
    Character[] key = new Character[10];

    public Line(Character letter, Character[] key) {
        this.letter = letter;
        this.key = key;
    }


    public Character getLetter() {
        return letter;
    }

    public void setLetter(Character letter) {
        this.letter = letter;
    }

    public Character[] getKey() {
        return key;
    }

    public void setKey(Character[] key) {
        this.key = key;
    }
}
