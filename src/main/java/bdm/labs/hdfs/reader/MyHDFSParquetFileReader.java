package bdm.labs.hdfs.reader;

import java.io.IOException;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;

public class MyHDFSParquetFileReader implements MyReader {
	
	private Configuration config;
	private FileSystem fs;
	
	ParquetReader<GenericRecord> parquetReader;
	
	public MyHDFSParquetFileReader() {
		try {
			this.config = new Configuration();
            config.addResource(new Path("/home/bdm/BDM-Software/hadoop-2.8.0/etc/hadoop/core-site.xml"));
            this.fs = FileSystem.get(config);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void open(String file) throws IOException {
		Path path = new Path(file);
		if (!this.fs.exists(path)) {
			System.out.println("File "+file+" does not exist!");
			System.exit(1);
		}
		parquetReader= new AvroParquetReader<GenericRecord>(path);
	}

	@Override
	public String next() throws IOException {
		GenericRecord gr = parquetReader.read();
		while(gr != null) {
			return gr.toString();
		}
		
		return null;
	}

	@Override
	public void close() throws IOException {
		this.parquetReader.close();
		this.fs.close();
	}

}
