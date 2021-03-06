package bdm.labs.hdfs;

import java.io.IOException;

import bdm.labs.hdfs.reader.MyHDFSAvroFileReader;
import bdm.labs.hdfs.reader.MyHDFSParquetFileReader;
import bdm.labs.hdfs.reader.MyHDFSPlainFileReader;
import bdm.labs.hdfs.reader.MyHDFSSequenceFileReader;
import bdm.labs.hdfs.reader.MyReader;
import bdm.labs.hdfs.writer.MyAvroFileWriter;
import bdm.labs.hdfs.writer.MyHDFSPlainFileWriter;
import bdm.labs.hdfs.writer.MyHDFSSequenceFileWriter;
import bdm.labs.hdfs.writer.MyParquetWriter;
import bdm.labs.hdfs.writer.MyWriter;
import wineinfo.avro.WineInfo;
import wineinfo.data_model.Generator;

public class Main {
		
	private static MyReader input;
	private static MyWriter output;
	private static String file;
	
	public static void read() throws IOException {
		input.open(file);
		String line = input.next();
		while (line != null) {
			if (!line.equals("")) {
				System.out.println(line);
			}
			line = input.next();
		}
		input.close();
	}
	
	public static void write(long number) throws IOException {
		output.open(file);
		for (int inst = 0; inst < number; ++inst) {
			WineInfo w = Generator.generateNewInstance(System.currentTimeMillis());
			output.put(w);
			output.flush();
		}
		output.close();
	}

	public static void main(String[] args) {
		try {
			if (args[0].equals("-write")) {
				//Possible formats
				if (args[1].equals("-plainText")) {
					output = new MyHDFSPlainFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-sequenceFile")) {
					output = new MyHDFSSequenceFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-avro")) {
					output = new MyAvroFileWriter();
					file = args[3];
				}
				else if (args[1].equals("-parquet")) {
					output = new MyParquetWriter();
					file = args[3];
				}
				
				write(Integer.parseInt(args[2]));
			}
			else if (args[0].equals("-read")) {
			    if (args[1].equals("-plainText")) {
                    input = new MyHDFSPlainFileReader();
                }
			    else if (args[1].equals("-sequenceFile")) {
					input = new MyHDFSSequenceFileReader();
				}
			    else if (args[1].equals("-avro")) {
			    	input = new MyHDFSAvroFileReader();
				}
				else if (args[1].equals("-parquet")) {
					input = new MyHDFSParquetFileReader();
				}

				file = args[2];
				read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
