# Branch-Prediction
Constructed a branch predictor simulator and use it to design branch predictors well suited to the SPECint95 benchmarks.



- To simulate a smith n-bit counter predictor: `sim smith <B> <tracefile>`, where B is the number of counter bits used for prediction.
- To simulate a bimodal predictor: `sim bimodal <M2> <tracefile>`, where M2 is the number of PC bits used to index the bimodal table.
- To simulate a gshare predictor: `sim gshare <M1> <N> <tracefile>`, where M1 and N are the number of PC bits and global branch history register bits used to index the gshare table, respectively.
- To simulate a hybrid predictor: `sim hybrid <K> <M1> <N> <M2> <tracefile>`, where K is the number of PC bits used to index the chooser table, M1 and N are the number of PC bits and global branch history register bits used to index the gshare table (respectively), and M2 is the number of PC bits used to index the bimodal table.
