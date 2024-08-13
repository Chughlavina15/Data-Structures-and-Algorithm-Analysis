/** Starter code for P3
 *  @author
 */

// Change to your net id
package lxc220045;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {
    
    // items storing their id and the item's description in the last
    // storing item's price at 0th position of the item's description list
    // {    item : [itemPrice, description of item from the list]   }
    HashMap<Integer, List<Integer>> items = new HashMap<>();

    // description and the corresponding item's id  
    // {    description : {item ids with this description}
    // creating Hashset for storing the item ids so that we O(1) lookup for any item id
    HashMap<Integer, HashSet<Integer>> descriptionDetails = new HashMap<>();

    // Constructors
    public MDS() {
    }

    /* Public methods of MDS. Do not change their signatures.
       __________________________________________________________________
       a. Insert(id,price,list): insert a new item whose description is given
       in the list.  If an entry with the same id already exists, then its
       description and price are replaced by the new values, unless list
       is null or empty, in which case, just the price is updated. 
       Returns 1 if the item is new, and 0 otherwise.
    */

    // checks whether an item description is present
    public boolean checkForItemInDescriptions(int itemDetail){
        return descriptionDetails.containsKey(itemDetail);
    }

    // checks whether an items with its id is present
    public boolean checkForItemIdInItems(int itemId){
        return items.containsKey(itemId);
    }
    
    public void addDescriptionDetails(int id, List<Integer> list){
        for(int itemDetail : list){
            boolean containsItem = checkForItemInDescriptions(itemDetail);
            if(containsItem == true){
                // storing items with this descritpion
                descriptionDetails.get(itemDetail).add(id);
            }
            else{
                // creating entry for the new item
                HashSet<Integer> idOfItems = new HashSet<>();
                idOfItems.add(id);
                descriptionDetails.put(itemDetail, idOfItems);
            }
        }
    }

    // removing the item id if a particular item description is removed
    public void removeDescriptionDetails(int id, List<Integer> list){
        for(int itemDetail : list){
            descriptionDetails.get(itemDetail).remove(id);
            }
    }

    public int insert(int id, int price, java.util.List<Integer> list) {
            boolean isItemPresent = checkForItemIdInItems(id);
            int valueToBeReturned;
            if(isItemPresent == false){
                // creating entry for new item
                addDescriptionDetails(id, list);
                valueToBeReturned = 1;
            }
            else{
                List<Integer> listOfOldItems =  items.get(id).subList(1, items.get(id).size());
                removeDescriptionDetails(id, listOfOldItems);
                addDescriptionDetails(id, list);
                valueToBeReturned = 0;
            }
            list.add(0,price);
            List<Integer> descriptionListOfNewItem = new ArrayList<>();
            for(int description: list){
                descriptionListOfNewItem.add(description);
            }
            items.put(id, descriptionListOfNewItem);
            return valueToBeReturned;
        }

    // b. Find(id): return price of item with given id (or 0, if not found).
    public int find(int id) {
        boolean containsItem = checkForItemIdInItems(id);
        int itemPrice = 0;
        if(containsItem == true){
            itemPrice = items.get(id).get(0);
        }
	    return itemPrice;
    }

    /* 
       c. Delete(id): delete item from storage.  Returns the sum of the
       ints that are in the description of the item deleted,
       or 0, if such an id did not exist.
    */
    public int delete(int id) {
        int sumOfDescriptions = 0;
        boolean isItemPresent = checkForItemIdInItems(id);
        if(isItemPresent == false){
            return 0;
        }
        List<Integer> itemDescripList = items.get(id);
        int itemPrice = itemDescripList.get(0);
        itemDescripList.remove(0);
        for(int i=0; i<itemDescripList.size(); i++){
            sumOfDescriptions += itemDescripList.get(i);
        }
        items.remove(id);
        removeDescriptionDetails(id, itemDescripList);
	    return sumOfDescriptions;
    }

    /* 
       d. FindMinPrice(n): given an integer, find items whose description
       contains that number (exact match with one of the ints in the
       item's description), and return lowest price of those items.
       Return 0 if there is no such item.
    */
    public int findMinPrice(int n) {
        int minPrice = Integer.MAX_VALUE;
        boolean containsItemDescription = checkForItemInDescriptions(n);
        if(containsItemDescription == false){
            minPrice = 0;
            return minPrice;
        }
        HashSet<Integer> listOfItems = descriptionDetails.get(n);
        for(int item: listOfItems){
            int itemPrice = items.get(item).get(0);
            if(itemPrice < minPrice){
                minPrice = itemPrice;
            }
        }
        return minPrice;
    }

    /* 
       e. FindMaxPrice(n): given an integer, find items whose description
       contains that number, and return highest price of those items.
       Return 0 if there is no such item.
    */
    public int findMaxPrice(int n) {
        int maxPrice = Integer.MIN_VALUE;
        boolean containsItemDescription = checkForItemInDescriptions(n);
        if(containsItemDescription == false){
            maxPrice = 0;
            return maxPrice;
        }
        HashSet<Integer> listOfItems = descriptionDetails.get(n);
        for(int item: listOfItems){
            int itemPrice = items.get(item).get(0);
            if(itemPrice > maxPrice){
                maxPrice = itemPrice;
            }
        }
        return maxPrice;
    }

    /* 
       f. FindPriceRange(n,low,high): given int n, find the number
       of items whose description contains n, and in addition,
       their prices fall within the given range, [low, high].
    */
    public int findPriceRange(int n, int low, int high) {
        boolean containsItemDescription = checkForItemInDescriptions(n);
        int numberOfItems = 0;
        if(containsItemDescription == false){
            return numberOfItems;
        }
        HashSet<Integer> itemsList = descriptionDetails.get(n);
        for(int item: itemsList){
            int itemPrice = items.get(item).get(0);
            if(itemPrice >= low && itemPrice <= high)
            { 
                numberOfItems+=1;   
            }
        }
        return numberOfItems;
	
    }

    /*
      g. RemoveNames(id, list): Remove elements of list from the description of id.
      It is possible that some of the items in the list are not in the
      id's description.  Return the sum of the numbers that are actually
      deleted from the description of id.  Return 0 if there is no such id.
    */
    public int removeNames(int id, java.util.List<Integer> list) {
        boolean isItemPresent = checkForItemIdInItems(id);
        if(isItemPresent == false){
            return 0;
        }
        int deletedDescriptions = 0;

        for(int itemDescription : list){
            boolean checkIfContainsDescription = checkForItemInDescriptions(itemDescription);
            if( checkIfContainsDescription == false ){
                    //System.err.println("continuing....");
                    continue;
            }
            HashSet<Integer> itemsList = descriptionDetails.get(itemDescription);
            if( itemsList.contains(id) == true ){
                deletedDescriptions = deletedDescriptions + itemDescription;
                itemsList.remove(id);
                items.get(id).remove(Integer.valueOf(itemDescription));
            }
        }
	    return deletedDescriptions;
    }

    public void printList(){
        System.out.println("Item List ");
        System.out.println(items);
        System.out.println("description details ");
        System.out.println(descriptionDetails);
    }
}

