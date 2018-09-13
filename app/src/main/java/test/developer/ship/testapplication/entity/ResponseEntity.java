package test.developer.ship.testapplication.entity;

import java.util.List;

/**
 * Created by Shiplayer on 12.09.18.
 */
public class ResponseEntity {
    private List<Offer> list;
    private Throwable throwable;

    public ResponseEntity(List<Offer> list) {
        this.list = list;
    }

    public ResponseEntity(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccessful(){
        return throwable == null;
    }

    public List<Offer> getList() {
        return list;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
