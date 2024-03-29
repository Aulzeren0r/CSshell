import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
/**

 * Implementation of the Searchable interface that searches a List of String objects.

 * This implementation searches only the beginning of the words, and is not be optimized

 * for very large Lists.

 * @author G. Cope

 *

 */

public class StringSearchable implements Searchable<String,String>{
    //Borrowed class.



    private List<String> terms = new ArrayList<String>();



    /**

     * Constructs a new object based upon the parameter terms.

     * @param terms The inventory of terms to search.

     */

    public StringSearchable(List<String> terms){

        this.terms.addAll(terms);

    }

    public void RemoveItem(String string){
        terms.remove(string);
    }


    @Override

    public Collection<String> search(String value) {

        List<String> founds = new ArrayList<String>();



        for ( String s : terms ){

            if ( s.indexOf(value) == 0 ){

                founds.add(s);

            }

        }

        return founds;

    }



}