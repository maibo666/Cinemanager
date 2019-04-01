package net.lzzy.cinemanager.models;

import net.lzzy.sqllib.Ignored;
import net.lzzy.sqllib.Sqlitable;
import net.lzzy.sqllib.Table;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by lzzy_gxy on 2019/3/11.
 * Description:
 */
@Table(name = "Orders")
public class Order extends BaseEntity implements Sqlitable, Collection<Order> {
    @Ignored
    static final String COL_MOVIE = "movie";
    @Ignored
    static final String COL_MOVIE_TIME = "movieTime";
    @Ignored
    static final String COL_PRICE = "price";
    @Ignored
    static final String COL_CINEMA_ID = "cinemaId";
    private String movie;
    private String movieTime;
    private float price;
    private UUID cinemaId;

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(String movieTime) {
        this.movieTime = movieTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public UUID getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(UUID cinemaId) {
        this.cinemaId = cinemaId;
    }

    @Override
    public boolean needUpdate() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Order> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Order order) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Order> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }
}
