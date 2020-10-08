/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import javafx.collections.ObservableList;

/**
 *
 * @author longh
 * @param <T>
 */
public interface DAO<T> {
    public ObservableList<T> getAll();
    public boolean create(T obj);
    public boolean update(T obj);
    public boolean delete(int id);
}
