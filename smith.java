import java.io.*;
import java.util.*;


public class smith {
    
    public static void display_results(ArrayList<String> original,String predictor,int right,int pointer,File fname){
        //total prediction available in the file
        int total_pred=original.size();
        //calc wrong predictions    
        int wrong = total_pred-right;
        System.out.println("COMMAND");
        System.out.println("./sim "+predictor +" " + pointer + " " + fname);
        System.out.println("OUTPUT");
        System.out.println("number of predictions:         " +total_pred);
        System.out.println("number of mispredictions:      " + wrong);
        System.out.println("misprediction rate:            " + String.format("%.2f", wrong*100.00 /total_pred) + "%");
    
    }

    public void main(String predictor,int pointer,char predicted_value,ArrayList<String> original,ArrayList<String> counter,File fname){

        int right=0;
        int flag=0;

        //let us cosider if the pointer goes through counter bits
        //if pointer is 1 then flag of counter value is 1, if 2 then 2, if 3 then 4 and 8 for pointer is 4
        if(pointer==1){
            flag=1;
        }
        else if(pointer==2){
            flag=2;
        }
        else if(pointer==3){
            flag=4;
        }
        else{
            flag=8;
        }
        int range=original.size();
        // loop through the original predictions and increment the 
        for(int a=0;a<range;a++){
            if(pointer==1){
                if(flag>0){
                    predicted_value='t';
                }
                else{
                    predicted_value='n';
                }
            }
            else if(pointer==2){
                if(flag>1){
                    predicted_value='t';
                }
                else{
                    predicted_value='n';
                }
            }
            else if(pointer==3){
                if(flag>=4){             //>=4 then it should consider 't' else 'n'
                    predicted_value='t';
                }
                else{
                    predicted_value='n';
                }
            }
            else{
                if(flag>7){
                    predicted_value='t';
                }
                else{
                    predicted_value='n';
                }
            }
            //increment the right counter value based on the prediction and outcome
            if(original.get(a).charAt(0)==predicted_value){
                right=right+1;
            }
            if(original.get(a).charAt(0)=='t'){
                if(pointer==1 &&flag!=1){
                    flag=flag+1;
                }
                else if(pointer==2 && flag!=3){
                    flag=flag+1;
                }
                else if(pointer==3 && flag!=7){
                    flag=flag+1;
                }
                else if(pointer==4 && flag!=15){
                    flag=flag+1;
                }
            }
            else if(flag!=0){
                flag=flag-1;
            }

        }
        //display the results
        display_results(original,predictor,right,pointer,fname);
        System.out.println("FINAL COUNTER CONTENT:         " + flag);
    }

    }
    
