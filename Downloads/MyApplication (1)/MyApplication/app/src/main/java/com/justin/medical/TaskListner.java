package com.justin.medical;

/**
 * Created by sys2025 on 12/9/15.
 */
public interface TaskListner {
    public void onTaskfinished(String response, int cd, String _className, String _methodName);
//    public void onTaskError(String error);
}
