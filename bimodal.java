import java.io.*;
import java.util.*;

public class bimodal {
    //populate position to be taken
    public int populate_position(String counter,int size){
        int position=0;
        StringBuilder builder=new StringBuilder();
        //shift by 2 to remove last 2 unwanted bits
        int addr= Integer.parseInt(counter,16);
        addr = addr >> 2;
        for(int a=size;a>0;a--){
            System.out.println("builder is: "+builder);
            System.out.println("addr is: "+addr);
            builder.append((addr&1));
            addr=addr>>1;
        }
        
        position=Integer.parseInt(builder.reverse().toString(),2);
        return position;
    }
    public static void display_results(ArrayList<String> original,String predictor,int right,int pointer,File fname){
        //total prediction available in the file
        int total_pred=original.size();
        //calc wrong predictions
        int wrong = total_pred - right;
        System.out.println("COMMAND");
        System.out.println("./sim "+predictor +" " + pointer + " " + fname);
        System.out.println("OUTPUT");
        System.out.println("number of predictions:         " + total_pred);
        System.out.println("number of mispredictions:      " + wrong);
        System.out.println("misprediction rate:            " + String.format("%.2f", wrong*100.00/total_pred) + "%");
    
    }

    public void main(String predictor,int pointer,HashMap<Integer,Integer> bimodal_counter,char predicted_value,ArrayList<String> original,ArrayList<String> counter,File fname){
        int right=0;


        for(int a=0;a<original.size();a++){
            int bimodal_label=populate_position(counter.get(a),pointer);
            
            //val should be >=4
            if(bimodal_counter.get(bimodal_label)>=4){
                predicted_value='t';
            }
            else{
                predicted_value='n';
            }
            //Increment the right counter
            if(original.get(a).charAt(0)==predicted_value){
                right=right+1;
            }

            //increment pointer values until the pointer reaches 7 when true
            if(original.get(a).charAt(0)=='t' && bimodal_counter.get(bimodal_label)!=7){ 
                bimodal_counter.put(bimodal_label,bimodal_counter.get(bimodal_label)+1);
            }
            else if(original.get(a).charAt(0)=='n' && bimodal_counter.get(bimodal_label)!=0){ //decrement pointer values until the pointer reaches 7 when true
                bimodal_counter.put(bimodal_label,bimodal_counter.get(bimodal_label)-1);

            }

        }   
        //display the results of command,predictions and mispredictions     
        // display_results(original,predictor,right,pointer,fname);
		// System.out.println("FINAL BIMODAL CONTENTS");
		// for(int i = 0; i < bimodal_counter.size(); i++){
		// 	System.out.println(i + "       " + bimodal_counter.get(i));
		// }
			
    }
    
}
