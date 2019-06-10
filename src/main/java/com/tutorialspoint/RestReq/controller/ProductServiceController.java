package com.tutorialspoint.RestReq.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.tutorialspoint.RestReq.model.Product;
import com.tutorialspoint.RestReq.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class ProductServiceController {
	
	@Autowired
	ProductService  productService;
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> delete (@PathVariable("id") String id){

		productService.deleteProduct(id);
		return new ResponseEntity<>("Product is deleted successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Object> updateProduct(@PathVariable("id") String id,@RequestBody Product product){
		
		productService.updateProduct(id, product);
		return new ResponseEntity<>("Product is updated successfully",HttpStatus.OK);
	}
	
	@RequestMapping(value="/products", method=RequestMethod.POST)
	public ResponseEntity<Object> createProduct(@RequestBody Product product)
	{
		productService.createProduct(product);
		return new ResponseEntity<> ("Product is created successfully" , HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/products")
	public ResponseEntity<Object> getProduct(){
		return new ResponseEntity<>(productService.getProducts(),HttpStatus.OK);
	}
	
	

	@RequestMapping(value="/upload" ,  method = RequestMethod.POST,consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
	public String fileUpload(@RequestParam("file") MultipartFile file) throws IOException{
		
		File convertFile = new File ("c:/fileupload/"+file.getOriginalFilename());
		convertFile.createNewFile();
		FileOutputStream fout = new FileOutputStream(convertFile);
		fout.write(file.getBytes());
		fout.close();
		return "File is upload successfully";		
		
	}
	
	@RequestMapping(value="/download" , method = RequestMethod.GET)
	public ResponseEntity<Object> downloadFile() throws IOException {
		String filename = "C:\\csb.log";
		File file = new File (filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);
		
		return responseEntity;
	}
}
