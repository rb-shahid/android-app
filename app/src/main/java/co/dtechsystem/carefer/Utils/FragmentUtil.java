package co.dtechsystem.carefer.Utils;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;



public class FragmentUtil {

    public static void addFragment(Context context, Fragment fragment, int layout){

        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout,fragment);
        transaction.commit();
    }
    public static void addFragment(Context context, Fragment fragment, int layout, Bundle bundle){
        fragment.setArguments(bundle);
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout,fragment);
        transaction.commit();
    }

    public static void addFragmentWithBackStack(Context context, Fragment fragment, int layout,String name){
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout,fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }
    public static void addFragmentWithBackStack(Context context, Fragment fragment, int layout,int name){
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout,fragment);
        transaction.addToBackStack(context.getResources().getString(name));
        transaction.commit();
    }
    public static void addFragmentWithBackStack(Context context, Fragment fragment, int layout,String name, Bundle bundle){
        fragment.setArguments(bundle);
        FragmentManager manager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout,fragment);
        transaction.addToBackStack(name);
        transaction.commit();
    }

    public static void replaceFragment(Context context, Fragment fragment, int layout){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).commit();
    }

    public static void replaceFragment(Context context, Fragment fragment, int layout, Bundle bundle){
        fragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).commit();
    }

    public static void replaceFragmentWithBackStack(Context context, Fragment fragment, int layout,String name){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).addToBackStack(name).commit();
    }

    public static void replaceFragmentWithBackStack(Context context, Fragment fragment, int layout,int name){
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).addToBackStack(String.valueOf(name)).commit();
    }
    public static void replaceFragmentWithBackStack(Context context, Fragment fragment, int layout,String name, Bundle bundle){
        fragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).addToBackStack(name).commit();
    }
    public static void replaceFragmentWithBackStack(Context context, Fragment fragment, int layout,int name, Bundle bundle){
        fragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(layout,fragment).addToBackStack(String.valueOf(name)).commit();
    }
}
