/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaguarlabs

class TagLib {
    static namespace = "TagLib"
    def redirectMainPage = {
      //  if(Session.user!=null)
      response.sendRedirect("${request.contextPath}/Peticion/list/")
    }
  }
