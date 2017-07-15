package cs499app.cs499mobileapp.model;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by centa on 7/12/2017.
 */

public class PlayQueue {

    private long currentPlaylistID;
    private List<StationRecord> stationQueue;
    private List<Integer> shuffleQueue;
    private boolean isShuffle;
    private boolean isRepeat;
    private int currentStationIndex;

    public PlayQueue() {
        currentPlaylistID = -1;
        currentStationIndex = -1;
        shuffleQueue = new ArrayList<>();
    }

    public void notifyPlayQueue(List<StationRecord> record, long currentPlaylistID,
                              int currentStationViewID, boolean newShuffleState,boolean newRepeatState)
    {
        Log.d("notifyPlayQueue","is Same playlist:"+ isSamePlaylist(currentPlaylistID));
        Log.d("notifyPlayQueue","ishuffle:"+ isShuffle);
        Log.d("notifyPlayQueue","newShuffleState:"+ newShuffleState);

        isRepeat = newRepeatState;
        stationQueue = record;

        if(!isSamePlaylist(currentPlaylistID)) {
            this.currentPlaylistID = currentPlaylistID;
            if(isShuffle == true && newShuffleState == true)
            {
                resetShuffleQueue();
                initShuffleQueue();
            }
        }
        this.currentStationIndex = currentStationViewID;
        setShuffle(newShuffleState);
        Log.d("Station Size:",stationQueue.size()+"");
    }

    // set it on will create shuffle list.
    // set it off will reset shuffle list
    public void setShuffle(boolean shuffleOn) {
        // if shuffle variable is changed
        if( isShuffle != shuffleOn) {
            if (shuffleOn == true) {
                initShuffleQueue();
            } else {
                shuffleQueue.clear();
            }
            this.isShuffle = shuffleOn;
        }
    }

    public void initShuffleQueue()
    {

        if(stationQueue != null) {
            for (int i = 0; i < stationQueue.size(); ++i) {
                shuffleQueue.add(i);
            }
            Collections.shuffle(shuffleQueue);
        }
    }

    public int getCurrentStationIndex() {
        return currentStationIndex;
    }

    public void setCurrentStationIndex(int currentStationIndex) {
        this.currentStationIndex = currentStationIndex;
    }

    public List<StationRecord> getStationQueue() {
        return stationQueue;
    }

    public void setStationQueue(List<StationRecord> stationQueue) {
        this.stationQueue = stationQueue;
    }

    public void resetShuffleQueue()
    {
        shuffleQueue.clear();
    }

    public void resetPlayQueue()
    {
        resetShuffleQueue();
    }


    // For when the new song is added, this will increment number of songs in list by 1.
    // reshuffle the shuffle list
    public void incrementStationEntry()
    {
        int newSize = stationQueue.size();
        shuffleQueue.add(newSize);
        Collections.shuffle(shuffleQueue);
    }

    // For when a song is removed, everything will reset and re-initialized.
    // if the song currently playing is not the one being removed.
    // add that current song index to the newly created history arraylist.
    public void removeIndex(int i)
    {
        resetPlayQueue();

        if(isShuffle == true)
        {
            initShuffleQueue();
        }
    }

    public void setPlayListViewID(long viewID)
    {
        currentPlaylistID = viewID;
    }

    public long getCurrentPlayListID() {
        return currentPlaylistID;
    }

    // return -1 if no next song
    public int getNextStation()
    {

        int size = stationQueue.size();
        int nextIndex = currentStationIndex + 1;
        int result = -1;

        if(isRepeat && nextIndex >= size)
            nextIndex = 0;

        // if nextIndex is within boundary of array
        if( nextIndex < size)
        {
            result = isShuffle  ? (shuffleQueue.get(nextIndex))
                                : (nextIndex);
            currentStationIndex = nextIndex;
        }
        return result;
    }

    // return -1 if no prev song
    public int getPrevStation()
    {
        int prevIndex = currentStationIndex - 1;
        int result = -1;

        if(isRepeat && prevIndex < 0)
            prevIndex = stationQueue.size()-1;

        // if nextIndex is within boundary of array
        if( prevIndex >= 0)
        {
            result = isShuffle  ? (shuffleQueue.get(prevIndex))
                    : (prevIndex);
            currentStationIndex = prevIndex;
        }
        return result;
    }

    public boolean isSamePlaylist(long currentPlaylistID)
    {
        boolean result = false;
        if(this.currentPlaylistID != currentPlaylistID)
        {
            result = false;
        }
        else
        {
            result = true;
        }
         return result;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }
}
