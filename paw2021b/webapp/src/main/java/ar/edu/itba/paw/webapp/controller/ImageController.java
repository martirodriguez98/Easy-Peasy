package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.webapp.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @RequestMapping(path = "recipes/images/{imageId}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE},
            method = RequestMethod.GET)
    @ResponseBody
    public byte[] getRecipeImage(@PathVariable("imageId") long imageId) {
        if(imageService.findById(imageId).isPresent()){
            Image image = imageService.findById(imageId).get();
            return image.getData();
        }else{
            throw new ResourceNotFoundException();
        }
    }
}