package com.graph.db.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.supercsv.io.dozer.CsvDozerBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.google.common.eventbus.Subscribe;
import com.graph.db.file.annotation.output.OutputFileType;

public class GenericSubscriber<T> implements AutoCloseable {
	
	protected CsvDozerBeanWriter beanWriter;

	public GenericSubscriber(String outputFolder, OutputFileType outputFileType) {
		try {
			FileWriter writer = new FileWriter(outputFolder + File.separator + outputFileType.getFileTag() + ".csv");
			beanWriter = new CsvDozerBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		beanWriter.configureBeanMapping(outputFileType.getBeanClass(), outputFileType.getHeader());
	}
	
    @Subscribe
    public void processAnnotation(T object) {
    	try {
			beanWriter.write(object);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }

	@Override
	public void close() {
		try {
			beanWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}