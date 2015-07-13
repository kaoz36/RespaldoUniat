package com.jaguarlabs.sipaccel.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.util.Log;

import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;
import com.jaguarlabs.sipaccel.vo.ValueVO;

public class CotizadorUtil {

	private static CotizadorUtil instanceCotizador;
	public static final int MASCULINO = R.id.radioMale;
	public static final int FEMENINO = R.id.radioFemale;
	
	private boolean doPFT;
	
	public CotizadorUtil() throws Exception{
		if(instanceCotizador != null){
			throw new Exception("Error de Singleton");
		}			
	}
	
	public static CotizadorUtil getInstance(){
		if( instanceCotizador == null ){
			try{
				instanceCotizador = new CotizadorUtil();
			}catch(Exception e){
				Log.e( "Error", e.getMessage() );
			}
		}
		return instanceCotizador;
	}
	
	public void setDoPFT( boolean doPFT ){
		this.doPFT = doPFT;
	}
	
	public boolean isDoPFT(){
		return doPFT;
	}
	
	public double getGFH(ValueVO<Double> gfhVO, double pGfh) {
		return getGFHFactor(gfhVO) * pGfh / 1000;
	}
	
	public double getGFHFactor(ValueVO<Double> gfhVO) {
		return gfhVO.getValue().floatValue();
	}
	
	public double getBACY(EdadVO pEdad, double pBacy) {
		return (getBACYFactor(pEdad)) * pBacy / 1000;
	}

	public double getBACYFactor(EdadVO pEdad) {
		if (doPFT) {
			return (pEdad.getBasf());
		}
		return (pEdad.getBasfn());
	}

	public double getBAS(EdadVO pEdad, ProfesionVO pProfesion, double pBas) {
		return (getBASFactor(pEdad, pProfesion)) * pBas / 1000;
	}
	
	public double getBASFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if (doPFT) {
			return (pEdad.getBas() + pProfesion.getMillar());
		}
		return (pEdad.getBasn() + pProfesion.getMillar());
	}
	
	
	public double getBASFFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if (doPFT) {
			return (pEdad.getBasf() + pProfesion.getMillar());
		}
		return (pEdad.getBasfn() + pProfesion.getMillar());
	}
	
	public double getCII(EdadVO pEdad, ProfesionVO pProfesion, double pCii) {
		return (getCIIFactor(pEdad, pProfesion) * pCii / 1000);
	}
	
	public double getCIIFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if( doPFT ){
			return (pEdad.getCii() * pProfesion.getInvalidez());
		}
		return (pEdad.getCiin() * pProfesion.getInvalidez());
	}

	
	public double getGFA(EdadVO pEdad, ProfesionVO pProfesion, double pGfa) {
			return (getBASFFactor( pEdad, pProfesion)) * pGfa / 1000;
	}
	
	
	
	public double getCMA(EdadVO pEdad, ProfesionVO pProfesion, double pCma) {
		return (getCMAFactor(pEdad, pProfesion) * pCma / 1000);
	}

	public double getCMAFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if (doPFT) {
			return (pEdad.getCma() * pProfesion.getAccidente());
		}
		return (pEdad.getCman() * pProfesion.getAccidente());
	}

	
	public double getTIBA(EdadVO pEdad, ProfesionVO pProfesion, double pTiba) {
		return (getTIBAFactor(pEdad,pProfesion) * pTiba / 1000);
	}

	public double getTIBAFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if (doPFT) {
			return (pEdad.getTiba() * pProfesion.getAccidente());
		}
		return (pEdad.getTiban() * pProfesion.getAccidente());
	}

	public double getCAT(EdadVO pEdad, double pCat, int pSexo) {
		return (getCATFactor( pEdad, pSexo) * pCat / 1000);
	}
	
	public double getCATFactor(EdadVO pEdad, int pSexo) {
		if( doPFT ){
			return ((pSexo == FEMENINO) ? pEdad.getCpcony() : pEdad.getBcat() );
		}
		return ((pSexo == FEMENINO) ? pEdad.getCpconyn() : pEdad.getBcatn() );
	}

	public double getGE(EdadVO pEdad, ProfesionVO pProfesion, double pGe) {
		return (getGEFactor(pEdad, pProfesion)) * pGe / 1000;
	}
	
	public double getGEFactor(EdadVO pEdad, ProfesionVO pProfesion) {
		if (doPFT) {
			return (pEdad.getGe() + pProfesion.getMillar());
		}
		return (pEdad.getGen() + pProfesion.getMillar());
	}
	
	public double redondea(double valor) {
		BigDecimal decimal = new BigDecimal(valor);
		return decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
}
