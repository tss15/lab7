package com.itmo.collection;

import com.itmo.Exceptions.NotYourPropertyException;
import com.itmo.client.User;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class LabWorkCollection implements Serializable {

    private Set<LabWork> labWorks;
    private final Date creationDate;


    public LabWorkCollection(Set<LabWork> labWorks){
        this.labWorks = Collections.synchronizedSet(labWorks);
        creationDate = new Date();
    }

    public String show(){
        StringBuilder builder = new StringBuilder();
        if(labWorks.size() == 0) return "Collection is empty. Please add something...";
        TreeSet<LabWork> treeSet = new TreeSet<>(labWorks);
        treeSet.forEach(d ->{
                builder.append("----------\n").append(d.toString()).append("\n");
        });
        return builder.toString();
    }
    public String clear(User user){
        Set<LabWork> set = filterOwnDragon(user);
        for(LabWork d : set){
            try {
                remove(d, user);
            } catch (NotYourPropertyException ignore) {
            }
        }
        return "Your LabWorks are cleared";
    }

    private Set<LabWork> filterOwnDragon(User user) {
        return Collections.synchronizedSet(labWorks).stream()
                .filter(d -> d.getOwnerName().equals(user.getName())).collect(Collectors.toSet());
    }

    public String add(LabWork labWork){
        Set<Long> setIds = Collections.synchronizedSet(labWorks).stream().map(LabWork::getId).collect(Collectors.toSet());
        // id generator
        for(long i = 0; i<Long.MAX_VALUE;i++){
            if(!setIds.contains(i)){
                labWork.setId(i);
                this.labWorks.add(labWork);
                return "LabWOrk added ";
            }
        }
        return "LabWork was not added because it was not possible to generate an id for it.";
    }
    public String addIfMax(LabWork labWork){
        if(isMax(labWork)){
            return add(labWork);
        }
        return "Not added since not max";
    }
    public boolean isMax(LabWork labWork){
        return (findMaxValue()< labWork.getValue());
    }
    public boolean isMin(LabWork labWork){
        return Collections.synchronizedSet(labWorks).stream().
                noneMatch(d -> (d.getValue()< labWork.getValue()));
    }
    public String addIfMin(LabWork labWork){
        if(isMin(labWork)){
            return "Not added since not min";
        }else{
            return add(labWork);
        }
    }

    /**
     * remove all elements from the collection that are less than the given one
     * @param labWork labwork with which all elements of the collection will be compared
     */
    public String removeLower(LabWork labWork, User user) {
        StringBuilder builder = new StringBuilder();
        filterOwnDragon(user).stream().filter(d -> d.getValue() < labWork.getValue())
                .forEach(dr -> {
                    builder.append("LabWork with that id is deleted ").append(dr.getId()).append("\n");
                    try {
                        remove(dr, user);
                    } catch (NotYourPropertyException e) {
                        e.printStackTrace();
                    }
                });
        if(builder.length()==0) return "No labworks less than specified";
        return builder.toString();
    }
    /**
     * filters the collection, leaves only those whose names start with name
     * @param name is the beginning of the name of the labworks to be obtained
     * @return set of labworks in filtered order
     */
    public Set<LabWork> filterStartsWithName(String name){
        return Collections.synchronizedSet(labWorks).stream()
                .filter(d -> d.getName().trim().startsWith(name)).collect(Collectors.toSet());
    }

    /**
     * a simple method to display a collection in reverse order
     */
    public String printDescending(){
        StringBuilder builder = new StringBuilder();
        Collections.synchronizedSet(labWorks).stream().sorted((o1, o2) -> (int) (o2.getValue()-o1.getValue()))
                .forEach(d -> builder.append(d.getName())
                        .append(" with value ").append(d.getValue()).append("\n"));
        return builder.toString();
    }
    public boolean removeById(long id, User user) throws NotYourPropertyException{
        LabWork labWork = findById(id);
        if(labWork != null){
            remove(labWork, user);
            return true;
        }
        return false;
    }

    public void remove(LabWork d, User user) throws NotYourPropertyException {
        if(!user.getName().equals(d.getOwnerName())){
            throw new NotYourPropertyException(d.getOwnerName());
        }
        this.labWorks.remove(d);
    }

    public float findMaxValue(){
        return (labWorks.size()==0 ? 0 :
                Collections.synchronizedSet(labWorks).stream()
                .max(Comparator.comparing(LabWork::getValue))
                        .get().getValue());
    }

    public LabWork findById(long id) {
        return labWorks.stream().filter(d -> d.getId() == id).findAny().orElse(null);
    }

    public String printFieldAscendingMinimalPoint(){
        StringBuilder builder = new StringBuilder();
        labWorks.stream().map(LabWork::getValue)
                .sorted()
                .forEach(v -> builder.append(v).append(" "));
        return builder.toString();
    }

    public String getCollectionInfo(){
        return "Collection type: com.itmo.LabWork\nDate of initialization: " + creationDate +
        "\nNumber of elements: " + labWorks.size();
    }

    public Set<LabWork> getLabWorks() {
        return labWorks;
    }
}