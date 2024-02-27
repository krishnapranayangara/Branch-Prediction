import java.io.*;
import java.util.*;

public class hybrid{

    //Populate chooser table with a size based on k bits
    public HashMap<Integer, Integer> populate_table(int size){
        double memory_size= Math.pow(2,size);
        HashMap<Integer, Integer> table= new HashMap<>();
        int i=0;
        while(i<memory_size){
            table.put(i,1);
            i=i+1;
        }
        return table;
    }
    
    //Populate gshare and bimodal Counters based on the m1 and m2 
    public HashMap<Integer, Integer> populate_counter(int size,HashMap<Integer, Integer>counter){
        double memory_size= Math.pow(2,size);
        int i=0;
        while(i<memory_size){
            counter.put(i,4);
            i=i+1;
        }
        return counter;
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
    public static void display_results(ArrayList<String> original,String predictor,int count,int k_bits,int m1,int share_bits,int m2,File fname){
        //total prediction available in the file
        int total_pred=original.size();
        //calc wrong predictions
        int wrong = total_pred - count;
		System.out.println("COMMAND");
		System.out.println("./sim " + predictor +" " +k_bits+" "+ m1 + " " + share_bits + " " + m2 + " " + fname);
		System.out.println("OUTPUT");
		System.out.println("number of predictions:         " + original.size());
		System.out.println("number of mispredictions:      " + wrong);
		System.out.println("misprediction rate:            " + String.format("%.2f", wrong*100.00/total_pred) + "%");
    
    }

    public void main(int k_bits,int m1,int share_bits,int m2,ArrayList<String> original,ArrayList<String> counter,String predictor,File fname){
    
    int register=0;

    //m1 is for gshare
    HashMap<Integer, Integer> gshare_counter=new HashMap<>();
    gshare_counter=populate_counter(m1,gshare_counter);


    //m2 is for bimodal
    HashMap<Integer, Integer> bimodal_counter=new HashMap<>();
    bimodal_counter=populate_counter(m2,bimodal_counter);


    //populate chooser table
    HashMap<Integer, Integer> table=populate_table(k_bits);

    //create filters
    int[] filters=populate_filters(share_bits);
    int filter1=filters[0];
    int filter2=filters[1];

    int count = 0;

    //read values
    for(int i = 0; i < original.size(); i++){
        //Bimodal prediction
        int bimodal_label = populate_position(counter.get(i), m2);
        char bpred = 's';
        if(bimodal_counter.get(bimodal_label)>=4){
            //taken if >=4
            bpred='t';
        }
        else{ 
            bpred='n';
        }
        //bimodal is taken if prediction is same as original prediction
        String bimodal="";
        if(bpred==original.get(i).charAt(0)){
            bimodal="taken";
        }else{
            bimodal="not taken";
        }


        //Gshare prediction
        char gpred = 'd';
        int gshare_label = populate_position(counter.get(i), m1);
        gshare_label=gshare_label^register;
        register=register>>1;
        if(gshare_counter.get(gshare_label)>=4){
            gpred='t';
        }
        else{
            gpred='n';
        }

        //gshare is taken if prediction is sam as original prediction
        String gshare="";
        if(gpred==original.get(i).charAt(0)){
            gshare="taken";
        }
        else{
            gshare="not taken";
        }
        int chooser_label = populate_position(counter.get(i), k_bits);
        //if table_label>=2 then consider gshare else bimodal
        int table_label=table.get(chooser_label);
        if(table_label >= 2){
            int gcounter=gshare_counter.get(gshare_label);
            if(original.get(i).charAt(0) == 't') {
                register = register | filter1;
                if (gshare_counter.get(gshare_label)<7){
                    gshare_counter.put(gshare_label,gcounter+1);
                }
            }
            else {
                register = register & filter2;
                if(gshare_counter.get(gshare_label) > 0) {
                    gshare_counter.put(gshare_label,gcounter-1);
                }
            }
            if(gshare == "taken") {
                count=count+1;
            }
        }
        else {
            int bcounter=bimodal_counter.get(bimodal_label);
            if(original.get(i).charAt(0) == 't') {
                register = register | filter1;
                if (bimodal_counter.get(bimodal_label) < 7){ 
                    bimodal_counter.put(bimodal_label,bcounter + 1);
                }
            }
            else { 
                register = register & filter2;
                if (bimodal_counter.get(bimodal_label) > 0){
                    bimodal_counter.put(bimodal_label,bcounter - 1);
                }
            }
            if(bimodal == "taken") {
                count=count+1;
            }
        }

       //Chooser table gets incremented when gshare is considered but gets decremented when bimodal gets taken
       //saturates at 0 and 3
       int label_value=table.get(chooser_label);
       if(label_value<3 && gshare=="taken" && bimodal =="not taken"){
           table.put(chooser_label,table.get(chooser_label)+1);
       }
       else if(label_value>0 && gshare=="not taken" && bimodal =="taken"){
           table.put(chooser_label,table.get(chooser_label)-1);
       }
    }
    
    

    
        //display results
        display_results(original, predictor, count,k_bits, m1, share_bits, m2, fname);
        System.out.println("FINAL CHOOSER CONTENTS");
        for(int a = 0; a < table.size(); a++){
			System.out.println(a + "      " + table.get(a));
		}
        System.out.println("FINAL GSHARE CONTENTS");
		for(int a = 0; a < gshare_counter.size(); a++){
			System.out.println(a + "      " + gshare_counter.get(a));
		}
		System.out.println("FINAL BIMODAL CONTENTS");
		for(int a = 0; a < bimodal_counter.size(); a++){
			System.out.println(a + "      " + bimodal_counter.get(a));
		}
    }
}

