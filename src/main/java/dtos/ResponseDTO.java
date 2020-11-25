/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dtos;

/**
 *
 * @author Tas
 */
public class ResponseDTO {

    private String Response;

    public ResponseDTO(String Response) {
        this.Response = Response;
    }

    
    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }



}
