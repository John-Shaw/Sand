package AST;

import java.util.Iterator;

/**
 * Created by John on 15/9/8.
 */
public abstract class ASTree implements Iterable<ASTree> {
    public abstract ASTree child(int i);
    public abstract int numChildren();
    public abstract Iterator<ASTree> children();
    public abstract String location();
    public Iterator<ASTree> iterable() {
        return children();
    }
}
