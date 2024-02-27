import java.io.*;
import java.util.*;

public class sim {
    public static void populate_instr(ArrayList<String> original,ArrayList<String> counter,File fname) throws FileNotFoundException{
        //take the trace file as value
        Scanner value=new Scanner(fname);
        //if the trace file is not empty
        while(value.hasNext()){
            String instr=value.next();
            if(instr.length()==1){
                original.add(instr);   //populate actual predictions 
            }
            else{
                counter.add(instr);    //populate instructions
            }
        } 
    }

    //populate counters
    public static HashMap<Integer, Integer> populate_pointer(int pointer,HashMap<Integer, Integer> counter){
        for(int a = 0; a < Math.pow(2, pointer); a++) {
            counter.put(a, 4);
        }
        return counter;
    }

    public static void main(String args[]) throws IOException{
        String predictor= args[0];
        int pointer=Integer.parseInt(args[1]);

        //inititalize the predicted value to some random character
        char predicted_value='s';
        
        ArrayList<String> original=new ArrayList<>();
        ArrayList<String> counter=new ArrayList<>();
        
        File fname=new File(args[args.length-1]);
        populate_instr(original,counter,fname);
        
        

// ---------------------------------------------  Smith -----------------------------------//
    if(predictor.equals("smith")){
        smith obj=new smith();
        obj.main( predictor, pointer, predicted_value,original, counter,fname);
    }
// ---------------------------------------------  bimodal  --------------------------------------//

    if(predictor.equals("bimodal")){
        
        //create counter table
        HashMap<Integer,Integer> bimodal_counter=new HashMap<>();
        bimodal_counter=populate_pointer(pointer, bimodal_counter);

        //call bimodal method
        bimodal obj=new bimodal();
        obj.main(predictor, pointer, bimodal_counter, predicted_value, original, counter, fname);
    }

// ---------------------------------------------  gshare  --------------------------------------//
    if(predictor.equals("gshare")){
        
        //check the gshare bits given by user
        int share_bits=Integer.parseInt(args[2]);
        
        //create counter table
        HashMap<Integer, Integer> gshare_counter = new HashMap<>();
        gshare_counter= populate_pointer(pointer,gshare_counter);

        //call gshare method
        gshare obj=new gshare();
        obj.main(predictor, pointer, share_bits, predicted_value, gshare_counter, original, counter, fname);

    }
// -----------------------------------------------hybrid-----------------------------------------//
    if(predictor.equals("hybrid")){
        
        //k bit for chooser table
        int k_bits=Integer.parseInt(args[1]);

        //M1 for gshare
        int m1=Integer.parseInt(args[2]);
        int share_bits=Integer.parseInt(args[3]);
        
        //M2 for bimodal
        int m2=Integer.parseInt(args[4]);

        //Call hybrid
        hybrid obj=new hybrid();
        obj.main(k_bits,m1,share_bits,m2,original,counter,predictor,fname);

    }
}

}

