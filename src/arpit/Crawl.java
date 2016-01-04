package arpit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author arpit
 */
class Fetch implements Runnable {

    Thread t;
    static List name = new ArrayList();
    static List rating = new ArrayList();
    Document doc = null;

    Fetch(String url) throws IOException {
        doc = Jsoup.connect(url).get();
        t = new Thread(this);
        t.start();
    }

    void fetchTitle() {
        Elements elementsName = doc.select("td[class=titleColumn]");
        for (Element outerName : elementsName) {
            Elements title = elementsName.select("a[title]");
            for (Element listName : title) {
                name.add(listName.text());
            }
        }
    }

    void fetchRating() {
        Elements elementsRating = doc.select("td[class=ratingColumn imdbRating]");
        for (Element listRating : elementsRating) {
            rating.add(Double.valueOf(listRating.text()));
        }
    }

    @Override
    public void run() {
        fetchTitle();
    }
}

public class Crawl {

    public static void main(String[] args) throws IOException {
        String url = "http://www.imdb.com/chart/top";
        Fetch ob = new Fetch(url);
        ob.fetchRating();
        try {
            ob.t.join();
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        Iterator itr1 = ob.name.iterator();
        Iterator itr2 = ob.rating.iterator();
        int count = 0;
        while (itr1.hasNext() && itr2.hasNext()) {
            System.out.printf("%-5d. %-70s\t%.1f\n", ++count, itr1.next(), itr2.next());
        }
    }
}
