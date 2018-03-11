package bdm.labs.hdfs.reader;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableInput;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import wineinfo.avro.WineInfo;
import org.apache.avro.mapred.FsInput;

public class MyHDFSAvroFileReader implements MyReader {

	private Configuration config;
	private FileSystem fs;

	DataFileReader<WineInfo> dataFileReader;
	
	public MyHDFSAvroFileReader() {
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
		DatumReader<WineInfo> wineInfoDatumReader= new SpecificDatumReader<WineInfo>(WineInfo.class);
		SeekableInput input = new FsInput(path, config);
		
		dataFileReader = new DataFileReader<WineInfo>(input, wineInfoDatumReader);
	}

	@Override
	public String next() throws IOException {
		while(dataFileReader.hasNext()) {
			return this.dataFileReader.next().toString();
		}
		
		return null;
	}

	@Override
	public void close() throws IOException {
		this.dataFileReader.close();
		this.fs.close();
	}

}
