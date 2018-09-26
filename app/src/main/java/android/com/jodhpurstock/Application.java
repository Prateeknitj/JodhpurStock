package android.com.jodhpurstock;

/**
 * Created by prateek on 16-Sep-18.
 */

public class Application {
    String name;
    String item;
    int quantity;
    String from;
    String to;
    String URL;
    String isSubmitted;

    public String isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(String submitted) {
        isSubmitted = submitted;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
