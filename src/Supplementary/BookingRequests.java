package Supplementary;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * <h1>Booking Requests Class</h1>
 * <p>This class is used to store the requests before the accepted or rejected by the admin.</p>
 */
public class BookingRequests implements Serializable{
    private ArrayList<HashMap> bookingrequests;
    public void serialize(ArrayList<HashMap> ar) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream("./src/DataFiles/bookingreqs.txt"));
            out.writeObject(bookingrequests);
        } finally {
            out.close();
        }
    }

    public ArrayList<HashMap> deserialize() throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        ArrayList<HashMap> books = null;
        try {
            in = new ObjectInputStream(new FileInputStream("./src/DataFiles/bookingreqs.txt"));
            books = (ArrayList<HashMap>) in.readObject();
        } finally {
            in.close();
        }
        return books;
    }

    public void addElement(HashMap<String, String> hm) {
        bookingrequests.add(hm);
    }

    public void setBookinRequests(ArrayList<HashMap> hm) {
        bookingrequests = new ArrayList<>(hm);
    }

    public void newBooking() {
        bookingrequests = new ArrayList<>();
    }

    public void removeBooking(HashMap<String, String> hm) {bookingrequests.remove(hm);}

    public ArrayList<HashMap> getBookingrequests() {
        return bookingrequests;
    }
}
