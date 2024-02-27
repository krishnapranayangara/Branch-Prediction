import java.io.*;
import java.util.*;

public class gshare{

    //Populate filters for updating the register
    public int[] populate_filters(int share_bits){
        int[] filter=new int[2];
        int i=1;

        StringBuilder builder=new StringBuilder();
        //Create filter1
        builder.append('1');
        while(i<share_bits){
            builder.append('0');
            String filter1=builder.toString();
            filter[0]=Integer.parseInt(filter1,2);
            i=i+1;
        }
        //Create filter2
        i=1;
        builder.append('0');
        while(i<share_bits){
            builder.append('1');
            String filter2=builder.toString();
            filter[1]=Integer.parseInt(filter2,2);
            i=i+1;
        }
        return filter;
    }

    //populate position to be taken
    public int populate_position(String counter,int size){
        int position=0;
        StringBuilder builder=new StringBuilder();
        //shift by 2 to remove last 2 unwanted bits
        int addr= Integer.parseInt(counter,16);
        addr = addr >> 2;
        for(int a=size;a>0;a--){
            builder.append((addr&1));
            addr=addr>>1;
        }
        position=Integer.parseInt(builder.reverse().toString(),2);
        return position;
    }

    //display results
    public static void display_results(ArrayList<String> original,String predictor,int share_bits,int right,int pointer,File fname){
        //total prediction available in the file
        int total_pred=original.size();
        //calc wrong predictions
        int wrong = total_pred - right;
        System.out.println("COMMAND");
        System.out.println("./sim "+predictor +" "+ pointer +" "+share_bits +" " + fname);
        System.out.println("OUTPUT");
        System.out.println("number of predictions:         " + total_pred);
        System.out.println("number of mispredictions:      " + wrong);
        System.out.println("misprediction rate:            " + String.format("%.2f", wrong*100.00/total_pred) + "%");
    
    }

    public void main(String predictor,int pointer,int share_bits,char predicted_value,HashMap<Integer, Integer> gshare_counter,ArrayList<String> original,ArrayList<String> counter,File fname){
        
        int register=0;
        int right=0;
        int gshare_label=0;

        //create filters
        int[] filters=populate_filters(share_bits);
        int filter1=filters[0];
        int filter2=filters[1];        
		
		for(int a=0;a<original.size();a++){

            gshare_label=populate_position(counter.get(a),pointer);
			gshare_label = gshare_label ^ register;

            //if counter value >=4 then predict as t
			if(gshare_counter.get(gshare_label) >= 4) {
                predicted_value = 't';
            }
			else {
                predicted_value = 'n';
            }
            if(original.get(a).charAt(0) == predicted_value) {
                right=right+1;
            }
            //saturates at 7 and 0
            register = register >> 1;
			if(original.get(a).charAt(0) == 't') {
				register = register | filter1;
				if (gshare_counter.get(gshare_label) != 7) {
                    gshare_counter.put(gshare_label, gshare_counter.get(gshare_label)+1);
                }
			}
			else {
				register = register & filter2;
				if(gshare_counter.get(gshare_label) != 0){
                    gshare_counter.put(gshare_label, gshare_counter.get(gshare_label)-1);
                }
			}
		}
		
		
        display_results(original, predictor,share_bits, right, pointer, fname);
        System.out.println("FINAL GSHARE CONTENTS");
		for(int a = 0; a < gshare_counter.size(); a++){
			System.out.println(a + "      " + gshare_counter.get(a));
		}
			
	}
    }

