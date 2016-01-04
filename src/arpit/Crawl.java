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
abstract class Fetch {

    Thread t;
    static List name = new ArrayList();
    static List rating = new ArrayList();
    Document doc = null;

    abstract void fetch();
}

class FetchRating extends Fetch implements Runnable {

    FetchRating(String url) throws IOException {
        doc = Jsoup.connect(url).get();
        t = new Thread(this);
        t.start();
    }

    @Override
    void fetch() {
        Elements elementsRating = doc.select("td[class=ratingColumn imdbRating]");
        for (Element listRating : elementsRating) {
            rating.add(Double.valueOf(listRating.text()));
        }
    }

    @Override
    public void run() {
        fetch();
    }

}

class FetchTitle extends Fetch implements Runnable {

    FetchTitle(String url) throws IOException {
        doc = Jsoup.connect(url).get();
        t = new Thread(this);
        t.start();
    }

    @Override
    void fetch() {
        Elements elementsName = doc.select("td[class=titleColumn]");
        for (Element outerName : elementsName) {
            Elements title = elementsName.select("a[title]");
            for (Element listName : title) {
                name.add(listName.text());
            }
        }
    }

    @Override
    public void run() {
        fetch();
    }
}

public class Crawl {

    public static void main(String[] args) throws IOException {
        String url = "http://www.imdb.com/chart/top";
        FetchTitle ob1 = new FetchTitle(url);
        FetchRating ob2 = new FetchRating(url);
        try {
            ob1.t.join();
            ob2.t.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Iterator itr1 = ob1.name.iterator();
        Iterator itr2 = ob1.rating.iterator();
        int count = 0;
        while (itr1.hasNext() && itr2.hasNext()) {
            System.out.printf("%-5d. %-70s\t%.1f\n", ++count, itr1.next(), itr2.next());
        }
    }
}
