import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ParallelWriter implements Runnable {

    private File file;
    private BlockingQueue<Item> q;
    private int indentation;

    public ParallelWriter( File f ){
        file = f;
        q = new LinkedBlockingQueue<Item>();
        indentation = 0;
    }

    public ParallelWriter append( CharSequence str ){
        try {
            CharSeqItem item = new CharSeqItem();
            item.content = str;
            item.type = ItemType.CHARSEQ;
            q.put(item);
            return this;
        } catch (InterruptedException ex) {
            throw new RuntimeException( ex );
        }
    }

    public ParallelWriter newLine(){
        try {
            Item item = new Item();
            item.type = ItemType.NEWLINE;
            q.put(item);
            return this;
        } catch (InterruptedException ex) {
            throw new RuntimeException( ex );
        }
    }

    public void setIndent(int indentation) {
        try{
            IndentCommand item = new IndentCommand();
            item.type = ItemType.INDENT;
            item.indent = indentation;
            q.put(item);
        } catch (InterruptedException ex) {
            throw new RuntimeException( ex );
        }
    }

    public void end(){
        try {
            Item item = new Item();
            item.type = ItemType.POISON;
            q.put(item);
        } catch (InterruptedException ex) {
            throw new RuntimeException( ex );
        }
    }

    public void run() {

        BufferedWriter out = null;
        Item item = null;

        try{
            out = new BufferedWriter( new FileWriter( file,true ) );
            while( (item = q.take()).type != ItemType.POISON ){
                switch( item.type ){
                    case NEWLINE:
                        out.newLine();
                        for( int i = 0; i < indentation; i++ )
                            out.append("   ");
                        break;
                    case INDENT:
                        indentation = ((IndentCommand)item).indent;
                        break;
                    case CHARSEQ:
                        out.append( ((CharSeqItem)item).content );
                }
            }
        } catch (InterruptedException ex){
            throw new RuntimeException( ex );
        } catch  (IOException ex) {
            throw new RuntimeException( ex );
        } finally {
            if( out != null ) try {
                out.close();
            } catch (IOException ex) {
                throw new RuntimeException( ex );
            }
        }
    }

    private enum ItemType {
        CHARSEQ, NEWLINE, INDENT, POISON;
    }
    private static class Item {
        ItemType type;
    }
    private static class CharSeqItem extends Item {
        CharSequence content;
    }
    private static class IndentCommand extends Item {
        int indent;
    }
    public int getSize(){
    	return q.size();
    }
}