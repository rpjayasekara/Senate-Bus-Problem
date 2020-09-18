import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * @author randika
 */
public class SenateBusProblem {

    public static int riders = 0;
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore multiplex = new Semaphore(50);
    public static Semaphore bus = new Semaphore(0);
    public static Semaphore allAboard = new Semaphore(0);

    public static ArrayList<Bus> bussesList = new ArrayList<Bus>();
    public static ArrayList<Rider> ridersList = new ArrayList<Rider>();

    private static ExponentialDistribution busArrivingExponentialDistribution;
    private static ExponentialDistribution riderArrivingExponentialDistribution;

    private static int busArrivingMeanTime = 1200;
    private static int riderArrivingMeanTime = 30;

    public static void main(String[] args) {
        System.out.println("test");
        busArrivingExponentialDistribution = new ExponentialDistribution(busArrivingMeanTime);
        riderArrivingExponentialDistribution = new ExponentialDistribution(riderArrivingMeanTime);
        start();
    }

    private static void start(){

        Thread busSpawner = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((long) busArrivingExponentialDistribution.sample());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Bus bus = new Bus(bussesList.size()+1);
                    bussesList.add(bus);
                    bus.start();
                }
            }
        });

        Thread riderSpawner = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep((long) riderArrivingExponentialDistribution.sample());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Rider rider = new Rider(ridersList.size()+1);
                    ridersList.add(rider);
                    rider.start();
                }
            }
        });

        busSpawner.start();
        riderSpawner.start();
    }

    private static class Bus extends Thread {
        private int busID;

        Bus(int id){
            this.busID = id;
        }

        @Override
        public void run(){
            try {
                mutex.acquire();
                System.out.println("Bus " + this.busID + " arrived." + riders + " riders are waiting to get in");
                System.out.print("Riders - ");
                if (riders > 0) {
                    bus.release();
                    allAboard.acquire();
                }
                mutex.release();
                System.out.println();
                System.out.println("Bus " + this.busID + " left the waiting area.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Rider extends Thread {
        private int riderID;

        Rider(int id){
           this.riderID = id;
        }

        @Override
        public void run(){
            try {
                multiplex.acquire();
                mutex.acquire();
                riders++;
                mutex.release();
                bus.acquire();
                multiplex.release();
                System.out.print(this.riderID + ",");
                riders--;
                if (riders == 0) {
                    allAboard.release();
                } else {
                    bus.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
