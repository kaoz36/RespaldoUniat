/** *************************************************************************
 *
 *   Copyright (c)  2013 by Jaguar Labs.
 *   Confidential and Proprietary
 *   All Rights Reserved
 *
 *   This software is furnished under license and may be used and copied
 *   only in accordance with the terms of its license and with the
 *   inclusion of the above copyright notice. This software and any other
 *   copies thereof may not be provided or otherwise made available to any
 *   other party. No title to and/or ownership of the software is hereby
 *   transferred.
 *
 *   The information in this software is subject to change without notice and
 *   should not be construed as a commitment by JaguarLabs.
 *
 * @(#)$Id: $
 * Last Revised By   : Raul Acevedo
 * Last Checked In   : $Date: $
 * Last Version      : $Revision:  $
 *
 * Original Author   : Julio Hernandez  julio.hernadez@jaguarlabs.com
 * Origin            : SEnE -- April 25 @ 11:00 (PST)
 * Notes             :
 *
 * *************************************************************************/

package com.jaguarlabs.sipaccel.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jaguarlabs.sipaccel.MainAplicationActivity;
import com.jaguarlabs.sipaccel.R;
import com.jaguarlabs.sipaccel.db.CommonDAO;
import com.jaguarlabs.sipaccel.db.SipacOpenHelper;
import com.jaguarlabs.sipaccel.dialog.CotizadorDataDialog;
import com.jaguarlabs.sipaccel.dialog.CotizadorDataDialogMas;
import com.jaguarlabs.sipaccel.model.DataModel;
import com.jaguarlabs.sipaccel.tables.CotizacionTable;
import com.jaguarlabs.sipaccel.util.CotizadorUtil;
import com.jaguarlabs.sipaccel.util.DelayedInitter;
import com.jaguarlabs.sipaccel.util.GEOmmittedError;
import com.jaguarlabs.sipaccel.util.IDialogDataRefresh;
import com.jaguarlabs.sipaccel.util.IKeyboardHider;
import com.jaguarlabs.sipaccel.util.IRangeCallback;
import com.jaguarlabs.sipaccel.util.OnFocusRangeChangeListener;
import com.jaguarlabs.sipaccel.util.OverFlowError;
import com.jaguarlabs.sipaccel.vo.CotizacionVO;
import com.jaguarlabs.sipaccel.vo.EdadVO;
import com.jaguarlabs.sipaccel.vo.ProfesionVO;
import com.jaguarlabs.sipaccel.vo.ValueVO;

public class CotizadorFragment extends RightFragment implements
		IDialogDataRefresh, IKeyboardHider,DelayedInitter {

	public double MAX = 3000000;
	public double MIN = 30000;
	public double MINGE = 10000;
	
	public static final int ENABLE_GPC = 8;
	public static final int ENABLE_CAT1 = 4;
	public static final int ENABLE_CAT2 = 2;
	public static final int ENABLE_CAT3 = 1;

	private OnFocusRangeChangeListener basHandler, ciiHandler, cmaHandler,
			tibaHandler, catHandler, gfaHandler, geHandler, bacyHandler,
			gfcHandler, gpcHandler, gfhHandler, ccompHandler, catcomp1Handler,
			catcomp2Handler, catcomp3Handler, conyugeEdadHandler,
			ccompEdadHandler, catcomp1EdadHandler, catcomp2EdadHandler,
			catcomp3EdadHandler, primaExcedenteHandler, primaTotalHandler;

	private CotizadorFragment mthis;
	private CotizadorDataDialog dialog;
	private CotizadorDataDialogMas dialogMas;

	private String fullName;
	
	private Spinner gfhSpinner, ccompSpinner, catcomp1Spinner, catcomp2Spinner,
			catcomp3Spinner, pagoSpinner, calculoSpiner;

	private TextView primaTotalT, extraPrimas;

	private EditText conyugeAge, ccompEdadInput, catcomp1EdadInput,
			catcomp2EdadInput, catcomp3EdadInput, basPrima, ciiPrima, cmaPrima,
			tibaPrima, catPrima, gfaPrima, gePrima, bacyPrima, gfcPrima,
			gpcPrima, gfhPrima, ccompPrima, catcomp1Prima, catcomp2Prima,
			catcomp3Prima, primaTotalInput, basSumaEdit, ciiSumaEdit,
			cmaSumaEdit, tibaSumaEdit, catSumaEdit, gfaSumaEdit, geSumaEdit,
			bacySumaEdit, gfcSumaEdit, gpcSumaEdit, gfhSumaEdit, ccompSumaEdit,
			catcomp1SumaEdit, catcomp2SumaEdit, catcomp3SumaEdit, bitSuma,
			bitPrima, primaExcedenteEdit;

	private CheckBox basCheck, ciiCheck, cmaCheck, tibaCheck, catCheck,
			gfaCheck, geCheck, conyugeCheck, bacyCheck, gfcCheck, gpcCheck,
			ccompCheck, catcomp1Check, catcomp2Check, catcomp3Check, bitCheck;

	private double bas, ge, cii, cma, tiba, cat, gfa, bacy, gfc, gpc, gfh,
			ccomp, catcomp1, catcomp2, catcomp3;

	private int sexo, edad, ccompEdad, edadConyugue, catcomp1Edad, catcomp2Edad,
			catcomp3Edad, estadosCP, nHijos;

	private double primaTotal = 0;
	private double primaExcedente = 0;

	private double primaTotalTem;
	private double bit = 0;

	private ProfesionVO selectedProfesion;
	private ValueVO<Double> selectedGFH;
	private ValueVO<Boolean> calculo;
	private ValueVO<Integer> catcompsexo, catcomp1sexo, catcomp2sexo, catcomp3sexo, pago;

	private boolean _fuma, dobas = true, docii, docma, dotiba, docat, dogfa,
			doedadcii, doedadcma, doedadtiba, doedadcat, doedadgpc, 
			doedadcatcomp1, doedadcatcomp2, doedadcatcomp3, doge, dobacy,
			doedadbit, doconyuge, dogfc, dogpc, doccomp, docatcomp1, 
			docatcomp2, docatcomp3, dobit, doNcotizacion, dogfh;

	private RadioGroup coberturasRadio, cotizacionRadio;
	
	private ArrayList<ValueVO<Integer>> values = new ArrayList<ValueVO<Integer>>();
	
	private Descuentos discounts;
	
	private class Descuentos{
		public static final int OVERFLOW_GFA = 1;
		public static final int OVERFLOW_CAT = 2;
		public static final int OVERFLOW_BACY = 4;
		public static final int OVERFLOW_GFC = 8;
		public static final int OVERFLOW_CATC = 16;
		public static final int OVERFLOW_GFH = 32;
		public static final int OVERFLOW_CCOM = 64;
		public static final int OVERFLOW_CATCOM1 = 128;
		public static final int OVERFLOW_CATCOM2 = 256;
		public static final int OVERFLOW_CATCOM3 = 512;
		public static final int OVERFLOW_CMA = 1024;
		public static final int OVERFLOW_TIBA = 2048;
		public static final int OVERFLOW_CII = 4096;
		public static final int OVERFLOW_BAS = 8192;

		double limiteCAT;
		double limiteGFA;
		double limiteBACY;
		double limiteGFC;
		double limiteCATC;
		double limiteGFH;
		double limiteCCOM;
		double limiteCATCOM1;
		double limiteCATCOM2;
		double limiteCATCOM3;
		double limiteCMA;
		double limiteTIBA;
		double limiteCII;
		double lowerLimit;
		
		int descuentos = 0, lowerOverflow = 0;
		int descuentosHechos = 0;

		public Descuentos(double limiteCAT, double limiteGFA, double limiteBACY,
				double limiteGFC, double limiteCATC, double limiteGFH,
				double limiteCCOM, double limiteCATCOM1, double limiteCATCOM2,
				double limiteCATCOM3, double limiteCMA, double limiteTIBA, double limiteCII, 
				double limiteBAS, double limiteGE) {
			super();
			this.limiteCAT = limiteCAT;
			this.limiteGFA = limiteGFA;
			this.limiteBACY = limiteBACY;
			this.limiteGFC = limiteGFC;
			this.limiteCATC = limiteCATC;
			this.limiteGFH = limiteGFH;
			this.limiteCCOM = limiteCCOM;
			this.limiteCATCOM1 = limiteCATCOM1;
			this.limiteCATCOM2 = limiteCATCOM2;
			this.limiteCATCOM3 = limiteCATCOM3;
			this.limiteCMA = limiteCMA;
			this.limiteTIBA = limiteTIBA;
			this.limiteCII = limiteCII;
			lowerLimit = 30000;
			reset();
		}

		
		public void setLimiteCMA( double limiteCMA ){
			this.limiteCMA = limiteCMA;
		}
		
		public void setLimiteTIBA( double limiteTIBA ){
			this.limiteTIBA = limiteTIBA;
		}
		
		public void setLimiteCII( double limiteCII ){
			this.limiteCII = limiteCII;
		}
		
		
		public double getLowerLimit(){
			return lowerLimit;
		}
		
		public double getLimiteCAT() {
			return limiteCAT;
		}

		public double getLimiteGFA() {
			return limiteGFA;
		}
		
		public double getLimiteBACY(){
			return limiteBACY;
		}
		
		public double getLimiteGFC(){
			return limiteGFC;
		}
		
		public double getLimiteCATC() {
			return limiteCATC;
		}

		public double getLimiteGFH() {
			return limiteGFH;
		}

		public double getLimiteCCOM() {
			return limiteCCOM;
		}

		public double getLimiteCATCOM1() {
			return limiteCATCOM1;
		}

		public double getLimiteCATCOM2() {
			return limiteCATCOM2;
		}

		public double getLimiteCATCOM3() {
			return limiteCATCOM3;
		}
		
		public double getLimiteCMA() {
			return limiteCMA;
		}
		
		public double getLimiteTIBA() {
			return limiteTIBA;
		}
		
		public double getLimiteCII(){
			return limiteCII;
		}

		public void checkLowerDescuentos(double pSuma)throws GEOmmittedError, OverFlowError{
			lowerOverflow = 0;
			if( (pSuma * (doNcotizacion ? .35 : 1) ) < lowerLimit && dogfa && doNcotizacion ){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_GFA);
			}else{
				desactivarLowerOverflow(OVERFLOW_GFA);
			}
			
			if( (pSuma * .35) < lowerLimit && dogfc && doNcotizacion ){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_GFC);
			}else{
				desactivarLowerOverflow(OVERFLOW_GFC);
			}
			
			if( (pSuma * .35 ) < lowerLimit && dogfh && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_GFH);
			}else{
				desactivarLowerOverflow(OVERFLOW_GFH);
			}
			
			if(pSuma < lowerLimit && docat && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CAT);
			}else{
				desactivarLowerOverflow(OVERFLOW_CAT);
			}
			
			if( (pSuma * (dogfc ? .65 : 1)) < lowerLimit && dobacy && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_BACY);
			}else{
				desactivarLowerOverflow(OVERFLOW_BACY);
			}
			
			if(pSuma < lowerLimit && dogpc && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CATC);
			}else{
				desactivarLowerOverflow(OVERFLOW_CATC);
			}
			//TODO:
			if(pSuma < lowerLimit && doccomp && doNcotizacion && ccompEdad < 18){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CCOM);
			}else{
				desactivarLowerOverflow(OVERFLOW_CCOM);
			}
			
			if(pSuma < lowerLimit && docatcomp1 && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CATCOM1);
			}else{
				desactivarLowerOverflow(OVERFLOW_CATCOM1);
			}
			
			if(pSuma < lowerLimit && docatcomp2 && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CATCOM2);
			}else{
				desactivarLowerOverflow(OVERFLOW_CATCOM2);
			}
			
			if(pSuma < lowerLimit && docatcomp3 && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CATCOM3);
			}else{
				desactivarLowerOverflow(OVERFLOW_CATCOM3);
			}
			
			if(pSuma < lowerLimit && docma && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CMA);
			}else{
				desactivarLowerOverflow(OVERFLOW_CMA);
			}
			
			if(pSuma < lowerLimit && dotiba && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_TIBA);
			}else{
				desactivarLowerOverflow(OVERFLOW_TIBA);
			}
			
			if(pSuma < lowerLimit && docii && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_CII);
			}else{
				desactivarLowerOverflow(OVERFLOW_CII);
			}
			
			if(pSuma < lowerLimit && dobas && doNcotizacion){
				if(doge == false){ throw new GEOmmittedError(); }
				activarLowerOverflow(OVERFLOW_BAS);
			}else{
				desactivarLowerOverflow(OVERFLOW_BAS);
			}
		}
		
		public void checkDescuentos(double pSuma){
			descuentos = 0;
			
			if( (pSuma * (doNcotizacion ? .35 : 1) ) > limiteGFA && dogfa){
				activarOverFlow(OVERFLOW_GFA);
			}else{
				desactivarOverFlow(OVERFLOW_GFA);
			}
			
			if( (pSuma * .35) > limiteGFC && dogfc){
				activarOverFlow(OVERFLOW_GFC);
			}else{
				desactivarOverFlow(OVERFLOW_GFC);
			}
			
			if( (pSuma * .35 ) > limiteGFH && dogfh){
				activarOverFlow(OVERFLOW_GFH);
			}else{
				desactivarOverFlow(OVERFLOW_GFH);
			}
			
			if(pSuma > limiteCAT && docat){
				activarOverFlow(OVERFLOW_CAT);
			}else{
				desactivarOverFlow(OVERFLOW_CAT);
			}
			
			if( (pSuma * (dogfc ? .65 : 1)) > limiteBACY && dobacy){
				activarOverFlow(OVERFLOW_BACY);
			}else{
				desactivarOverFlow(OVERFLOW_BACY);
			}
			
			if(pSuma > limiteCATC && dogpc){
				activarOverFlow(OVERFLOW_CATC);
			}else{
				desactivarOverFlow(OVERFLOW_CATC);
			}
			
			//TODO: 
			if(pSuma > limiteCCOM && doccomp && ccompEdad < 18){
				activarOverFlow(OVERFLOW_CCOM);
			}else{
				desactivarOverFlow(OVERFLOW_CCOM);
			}
			
			if(pSuma > limiteCATCOM1 && docatcomp1){
				activarOverFlow(OVERFLOW_CATCOM1);
			}else{
				desactivarOverFlow(OVERFLOW_CATCOM1);
			}
			
			if(pSuma > limiteCATCOM2 && docatcomp2){
				activarOverFlow(OVERFLOW_CATCOM2);
			}else{
				desactivarOverFlow(OVERFLOW_CATCOM2);
			}
			
			if(pSuma > limiteCATCOM3 && docatcomp3){
				activarOverFlow(OVERFLOW_CATCOM3);
			}else{
				desactivarOverFlow(OVERFLOW_CATCOM3);
			}
			
			if(pSuma > limiteCMA && docma){
				activarOverFlow(OVERFLOW_CMA);
			}else{
				desactivarOverFlow(OVERFLOW_CMA);
			}
			
			if(pSuma > limiteTIBA && dotiba){
				activarOverFlow(OVERFLOW_TIBA);
			}else{
				desactivarOverFlow(OVERFLOW_TIBA);
			}
			
			if(pSuma > limiteCII && docii){
				activarOverFlow(OVERFLOW_CII);
			}else{
				desactivarOverFlow(OVERFLOW_CII);
			}
		}
		
		public void resetAll(){
			descuentos = descuentosHechos = 0;
		}
		
		public void reset(){
			descuentosHechos = 0;
		}
		
		private void hacerDescuento(int constante) {
			descuentosHechos = (descuentosHechos | constante);
		}
		
		private void activarLowerOverflow( int constante) {
			lowerOverflow = (lowerOverflow | constante);
		}

		private void desactivarLowerOverflow(int constante) {
			lowerOverflow = (~constante & lowerOverflow);
		}
		
		public boolean isActivoLowerOverflow(int bandera){
			return ((lowerOverflow & bandera) == bandera);
		}
		
		
		private void activarOverFlow( int constante) {
			descuentos = (descuentos | constante);
		}

		private void desactivarOverFlow(int constante) {
			descuentos = (~constante & descuentos);
		}
		
		public boolean isActivoOverflow(int bandera){
			return ((descuentos & bandera) == bandera);
		}
		
		public boolean isValid(){
			return ((~descuentosHechos & (descuentos | lowerOverflow)) == 0);
		}
	}
	
	public void hideKeyboard(EditText currentField){
		InputMethodManager imm = (InputMethodManager)caller.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(currentField != null)
			imm.hideSoftInputFromWindow( currentField.getWindowToken(), 0);
	}

	
	private ProgressDialog pDialog;
	private List<ViewGroup> cotizadorPages;
	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mthis = this;
		try {
			dialog = new CotizadorDataDialog(caller, mthis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mainView = inflater.inflate(
				com.jaguarlabs.sipaccel.R.layout.fragment_cotizacion,
				container, false);
		ViewGroup flipper = (ViewGroup)mainView.findViewById(R.id.pager_title_strip_flip);
		flipper.setVisibility(View.INVISIBLE);
		pDialog = ProgressDialog.show(caller.getMContext(), "Progreso",
				"Cargando Cotizador", true);
		
		(new CotizadorInflateTask(this)).execute();
	
	}
	
	@Override
	public void delayedInit(View pView) {
		ViewGroup flipper = (ViewGroup)mainView.findViewById(R.id.pager_title_strip_flip);
		for(ViewGroup item:cotizadorPages)
		{
			flipper.addView(item);
		}
		dialogMas = new CotizadorDataDialogMas(caller, mthis);
		discounts = new Descuentos(1000000, 180000, 344760, 180000, 1000000, 180000, 
				MAX, 1000000, 1000000, 1000000, MAX, MAX, MAX, MAX, MAX);
		ImageButton back;
		MainAplicationActivity.MainAppTouch interaction = new MainAplicationActivity.MainAppTouch(
				listener);
		
		back = (ImageButton) mainView
		.findViewById(com.jaguarlabs.sipaccel.R.id.back);
		back.setOnTouchListener(interaction);
		back.setOnClickListener(interaction);
		setters();
		
		
		flipper.setVisibility(View.VISIBLE);
		pDialog.cancel();
	}
	
	@Override
	public View inProcess() {
		caller.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				try{		
					cotizadorPages = new ArrayList<ViewGroup>();
					cotizadorPages.add((ViewGroup)caller.getLayoutInflater().inflate(R.layout.page_cotizador_titular, null));
					cotizadorPages.add((ViewGroup)caller.getLayoutInflater().inflate(R.layout.page_cotizador_cobertura, null));
					cotizadorPages.add((ViewGroup)caller.getLayoutInflater().inflate(R.layout.page_cotizador_conyugue, null));
					cotizadorPages.add((ViewGroup)caller.getLayoutInflater().inflate(R.layout.page_cotizador_hijos, null));
					}catch(Exception error)
					{
						Log.e("Error", "Error Generando cotizador");
					}
			}
		});
		return null;
	}	
		

	private void setters() {

		primaTotalT = (TextView) mainView.findViewById(R.id.primaTotalText);
		extraPrimas = (TextView) mainView.findViewById(R.id.extraPrimas);
		
		primaExcedenteEdit = (EditText) mainView.findViewById(R.id.primaExcedenteInput);

		basPrima = (EditText) mainView.findViewById(R.id.basPrima);
		ciiPrima = (EditText) mainView.findViewById(R.id.ciiPrima);
		cmaPrima = (EditText) mainView.findViewById(R.id.cmaPrima);
		tibaPrima = (EditText) mainView.findViewById(R.id.tibaPrima);
		catPrima = (EditText) mainView.findViewById(R.id.catPrima);
		gfaPrima = (EditText) mainView.findViewById(R.id.gfaPrima);
		gePrima = (EditText) mainView.findViewById(R.id.gePrima);

		bacyPrima = (EditText) mainView.findViewById(R.id.bacyPrima);
		gfcPrima = (EditText) mainView.findViewById(R.id.gfcPrima);
		gpcPrima = (EditText) mainView.findViewById(R.id.gpcPrima);
		gfhPrima = (EditText) mainView.findViewById(R.id.gfhPrima);
		ccompPrima = (EditText) mainView.findViewById(R.id.ccompPrima);
		catcomp1Prima = (EditText) mainView.findViewById(R.id.catcomp1Prima);
		catcomp2Prima = (EditText) mainView.findViewById(R.id.catcomp2Prima);
		catcomp3Prima = (EditText) mainView.findViewById(R.id.catcomp3Prima);

		bitSuma = (EditText) mainView.findViewById(R.id.bitSuma);
		bitPrima = (EditText) mainView.findViewById(R.id.bitPrima);

		primaTotalInput = (EditText) mainView
				.findViewById(R.id.primaTotalInput);

		gfhSpinner = (Spinner) mainView.findViewById(R.id.gfhSpinner);
		ccompSpinner = (Spinner) mainView.findViewById(R.id.ccompSpinner);
		catcomp1Spinner = (Spinner) mainView.findViewById(R.id.catcomp1Spinner);
		catcomp2Spinner = (Spinner) mainView.findViewById(R.id.catcomp2Spinner);
		catcomp3Spinner = (Spinner) mainView.findViewById(R.id.catcomp3Spinner);
		pagoSpinner = (Spinner) mainView.findViewById(R.id.pagoSpinner);
		calculoSpiner = (Spinner) mainView.findViewById(R.id.calculoSpinner);

		basCheck = (CheckBox) mainView.findViewById(R.id.basCheck);
		ciiCheck = (CheckBox) mainView.findViewById(R.id.ciiCheck);
		cmaCheck = (CheckBox) mainView.findViewById(R.id.cmaCheck);
		tibaCheck = (CheckBox) mainView.findViewById(R.id.tibaCheck);
		catCheck = (CheckBox) mainView.findViewById(R.id.catCheck);
		gfaCheck = (CheckBox) mainView.findViewById(R.id.gfaCheck);
		geCheck = (CheckBox) mainView.findViewById(R.id.geCheck);

		conyugeCheck = (CheckBox) mainView.findViewById(R.id.conyugeCheck);
		bacyCheck = (CheckBox) mainView.findViewById(R.id.bacyCheck);
		gfcCheck = (CheckBox) mainView.findViewById(R.id.gfcCheck);
		gpcCheck = (CheckBox) mainView.findViewById(R.id.gpcCheck);
		bitCheck = (CheckBox) mainView.findViewById(R.id.bitCheck);

		ccompCheck = (CheckBox) mainView.findViewById(R.id.ccompCheck);
		catcomp1Check = (CheckBox) mainView.findViewById(R.id.catcomp1Check);
		catcomp2Check = (CheckBox) mainView.findViewById(R.id.catcomp2Check);
		catcomp3Check = (CheckBox) mainView.findViewById(R.id.catcomp3Check);

		coberturasRadio = (RadioGroup) mainView
				.findViewById(R.id.coberturasRadio);
		cotizacionRadio = (RadioGroup) mainView
				.findViewById(R.id.cotizacionRadio);

		conyugeAge = (EditText) mainView.findViewById(R.id.conyugeAge);
		ccompEdadInput = (EditText) mainView.findViewById(R.id.ccompEdad);

		basSumaEdit = (EditText) mainView.findViewById(R.id.basSuma);
		ciiSumaEdit = (EditText) mainView.findViewById(R.id.ciiSuma);
		cmaSumaEdit = (EditText) mainView.findViewById(R.id.cmaSuma);
		tibaSumaEdit = (EditText) mainView.findViewById(R.id.tibaSuma);
		catSumaEdit = (EditText) mainView.findViewById(R.id.catSuma);
		gfaSumaEdit = (EditText) mainView.findViewById(R.id.gfaSuma);
		geSumaEdit = (EditText) mainView.findViewById(R.id.geSuma);
		bacySumaEdit = (EditText) mainView.findViewById(R.id.bacySuma);
		gfcSumaEdit = (EditText) mainView.findViewById(R.id.gfcSuma);
		gpcSumaEdit = (EditText) mainView.findViewById(R.id.gpcSuma);
		gfhSumaEdit = (EditText) mainView.findViewById(R.id.gfhSuma);
		ccompSumaEdit = (EditText) mainView.findViewById(R.id.ccompSuma);
		catcomp1SumaEdit = (EditText) mainView.findViewById(R.id.catcomp1Suma);
		catcomp2SumaEdit = (EditText) mainView.findViewById(R.id.catcomp2Suma);
		catcomp3SumaEdit = (EditText) mainView.findViewById(R.id.catcomp3Suma);
		catcomp1EdadInput = (EditText) mainView.findViewById(R.id.catcomp1Edad);
		catcomp2EdadInput = (EditText) mainView.findViewById(R.id.catcomp2Edad);
		catcomp3EdadInput = (EditText) mainView.findViewById(R.id.catcomp3Edad);

		settersFocus();

		setUpCalculo();
		setUpPago();
		setUpHijos();
		setUpSexo();
		setUpProfesiones();
		setPages();

		initalizeVariables();
		
		getListeners();
	}
	
	private void initalizeVariables(){
		calculoSpiner.setSelection( 0 );
		cambioCalculo();		
	}
	
	private void cambioCalculo(){
		fullName = "";
		primaExcedente = 0;
		estadosCP = 0;
		edad = 15;
		edadConyugue = 15;
		ccompEdad = 15;
		catcomp1Edad = 15;
		catcomp2Edad = 15;
		catcomp3Edad = 15;		
		sexo = CotizadorUtil.MASCULINO;
		catcompsexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp1sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp2sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp3sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		_fuma = true;
		CotizadorUtil.getInstance().setDoPFT( true );		
		doNcotizacion = true;
		primaTotal = 0;
		primaTotalTem = 0;
		estadosCP = 0;
		cotizacionRadio.check( R.id.radNP );
		coberturasRadio.check( R.id.radCP );		
		pagoSpinner.setSelection( 0 );
		gfhSpinner.setSelection( 0 );
		ccompSpinner.setSelection( 0 );
		catcomp1Spinner.setSelection( 0 );
		catcomp2Spinner.setSelection( 0 );
		catcomp3Spinner.setSelection( 0 );
		dobas = true;
		dobit = false;
		docii = false;
		docma = false;
		dotiba = false;
		docat = false;
		doge = false;
		doconyuge = false;
		dogfh = false;
		dobacy = false;
		dogpc = false;
		doccomp = false;
		docatcomp1 = false;
		docatcomp2 = false;
		docatcomp3 = false;
		bas = 100000;
		bacy = 0;
		gfa = 0;
		ge = 0;
		gfc = 0;
		bit = 0;	
		refreshCotizador();
	}
	
	private void refreshCotizador(){
		dogfa = false;
		dogfc = false;
		dogfh = false;
		refresh();
	}
	
	private void settersFocus() {
		
		primaExcedenteHandler = new OnFocusRangeChangeListener<Double>( 0.0, MAX,
				new IRangeCallback() {
			@Override
			public void doAfterValidation(double value, boolean valid) {
				primaExcedente = primaExcedenteEdit.getText().toString().equals("") ? 0.0 : value;				
				refresh();
			}
		}, primaExcedenteEdit, this);
		
		primaTotalHandler = new OnFocusRangeChangeListener<Double>(new Double(1), MAX, 
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						if( primaTotalInput.getText().toString().equals("") ){
							value = 0;
						}
						primaTotal = value;
						refresh();
					}
			  	}, primaTotalInput, this);

		basHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						bas = (valid ? value : (value <= MIN) ? MIN : MAX);
						refresh();
					}
				}, basSumaEdit,this);

		//TODO:
		geHandler = new OnFocusRangeChangeListener<Double>(MINGE, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ge = (valid ? value :  doNcotizacion ? Math.min(
								Math.max(MINGE,value),bas) : Math.min(
								Math.max(MINGE,value),MAX));
						refresh();
					}
				}, geSumaEdit,this);

		ciiHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cii = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),MAX));
						refresh();
					}
				}, ciiSumaEdit,this);

		cmaHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cma = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),MAX));
						refresh();
					}
				}, cmaSumaEdit,this);

		tibaHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						tiba = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),MAX));
						refresh();
					}
				}, tibaSumaEdit,this);

		catHandler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cat = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),1000000.0));
						refresh();
					}
				}, catSumaEdit,this);

		gfaHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfa = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value), (int) (bas * .35)) : Math.min(Math.max(MIN,value), 180000.0));
						refresh();
					}
				}, gfaSumaEdit,this);

		bacyHandler = new OnFocusRangeChangeListener<Double>(MIN, 344760.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						bacy = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),MAX));
						refresh();
					}
				}, bacySumaEdit,this);

		gfcHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfc = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value), (int) (bas * .35)) : Math.min(Math.max(MIN,value), 180000.0));
						refresh();
					}
				}, gfcSumaEdit,this);

		catcomp1Handler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp1 = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),1000000.0));
						refresh();
					}
				}, catcomp1SumaEdit,this);

		catcomp2Handler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp2 = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),1000000.0));
						refresh();
					}
				}, catcomp2SumaEdit,this);

		catcomp3Handler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp3 = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value),1000000.0));
						refresh();
					}
				}, catcomp3SumaEdit,this);

		gfhHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfh = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value), (int) (bas * .35) ) : Math.min(Math.max(MIN,value), 180000.0));
						refresh();
					}
				}, gfhSumaEdit,this);

		
		gpcHandler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gpc = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN, value), bas) : Math.min(Math.max(MIN,value), 1000000.0));
						refresh();
					}
				}, gpcSumaEdit,this);

		ccompHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ccomp = valid ? value : (doNcotizacion ? Math.min(Math.max(MIN,value),bas) : Math.min(Math.max(MIN,value), (ccompEdad < 18 ? 3000000 : 100000000 )));
						refresh();
					}
				}, ccompSumaEdit,this);

		conyugeEdadHandler = new OnFocusRangeChangeListener<Integer>(15, 70,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						edadConyugue = (int) (valid ? value : (value <= 15) ? 15
								: value);
						refresh();
					}
				}, conyugeAge,this);

		ccompEdadHandler = new OnFocusRangeChangeListener<Integer>(15, 70,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ccompEdad = (int) (valid ? value : (value <= 15) ? 15
								: value);
						refresh();
					}
				}, ccompEdadInput,this);

		catcomp1EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp1Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp1EdadInput,this);

		catcomp2EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp2Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp2EdadInput,this);

		catcomp3EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp3Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp3EdadInput,this);

		primaTotalInput.setOnFocusChangeListener(primaTotalHandler);

		primaExcedenteEdit.setOnFocusChangeListener(primaExcedenteHandler);
		
		gfcSumaEdit.setOnFocusChangeListener(gfcHandler);

		bacySumaEdit.setOnFocusChangeListener(bacyHandler);

		conyugeAge.setOnFocusChangeListener(conyugeEdadHandler);

		catcomp1SumaEdit.setOnFocusChangeListener(catcomp1Handler);

		catcomp2SumaEdit.setOnFocusChangeListener(catcomp2Handler);

		catcomp3SumaEdit.setOnFocusChangeListener(catcomp3Handler);

		ccompSumaEdit.setOnFocusChangeListener(ccompHandler);

		gfhSumaEdit.setOnFocusChangeListener(gfhHandler);

		gpcSumaEdit.setOnFocusChangeListener(gpcHandler);

		ccompEdadInput.setOnFocusChangeListener(ccompEdadHandler);

		catcomp1EdadInput.setOnFocusChangeListener(catcomp1EdadHandler);

		catcomp2EdadInput.setOnFocusChangeListener(catcomp2EdadHandler);

		catcomp3EdadInput.setOnFocusChangeListener(catcomp3EdadHandler);

		basSumaEdit.setOnFocusChangeListener(basHandler);

		geSumaEdit.setOnFocusChangeListener(geHandler);

		gfaSumaEdit.setOnFocusChangeListener(gfaHandler);

		catSumaEdit.setOnFocusChangeListener(catHandler);

		tibaSumaEdit.setOnFocusChangeListener(tibaHandler);

		cmaSumaEdit.setOnFocusChangeListener(cmaHandler);

		ciiSumaEdit.setOnFocusChangeListener(ciiHandler);
	}

	public void getListeners() {

		((TextView) mainView.findViewById(R.id.textMas))
				.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						dialogMas.refresh( Double.parseDouble( primaTotalInput.getText().toString() ), pago.getValue().intValue() );
						dialogMas.show();
					}
				});
		
		((Button) mainView.findViewById(R.id.dataButton))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.show();
					}
				});
		
		((Button) mainView.findViewById(R.id.clearButton))
				.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initalizeVariables();
				gfhSumaEdit.setTextColor( Color.parseColor("#818084"));
			}
		});
		
		((Button) mainView.findViewById(R.id.showCotizacionButton))
		.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCotizacion();
			}
		});
	}
	//TODO:
	private void showCotizacion(){
		
		CotizacionVO cotizacionVO = new CotizacionVO
		(fullName, selectedProfesion, _fuma, dobas, dobit, docii,
				docma, dotiba, docat, dogfa, doge, doconyuge, dobacy, dogfc,
				dogpc, dogfh, CotizadorUtil.getInstance().isDoPFT(), doccomp,
				docatcomp1, docatcomp2, docatcomp3,	sexo, catcompsexo, 
				catcomp1sexo, catcomp2sexo, catcomp3sexo, getEdadVO(edad), 
				getEdadCalculoVO(edad, sexo, _fuma), getEdadVO(edadConyugue), getEdadVO(ccompEdad), 
				getEdadVO(catcomp1Edad), getEdadVO(catcomp2Edad), getEdadVO(catcomp3Edad), 
				nHijos,	primaExcedente, (calculo.getValue() ? primaTotal  : 
					(primaExcedente + primaTotal ) / pago.getValue().intValue() ),
				bas, bit, cii, cma, tiba, cat, gfa,	ge, bacy, gfc, gpc, 
				gfh, ccomp, catcomp1, catcomp2, catcomp3, selectedGFH, pago.getLabel() );
		
		DataModel.getInstance().setEstadoCotizador(cotizacionVO);
		
		Intent intent = new Intent( mainView.getContext(), CotizacionTable.class );
		
		startActivity( intent );
	}
	
	private void setPages() {
		pageTitular();
		pageCobertura();
		pageconyuge();
		pageHijos();
	}

	private int prenderBandera(int bandera, int constante) {
		return (bandera | constante);
	}

	private int apagarBandera(int bandera, int constante) {
		return (~constante & bandera);
	}

	private void pageTitular() {

		calculoSpiner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				calculo = (ValueVO<Boolean>) parent.getItemAtPosition(pos);
				cambioCalculo();
				
				if (calculo.getValue()) {
					pago = values.get(0);
					pagoSpinner.setSelection(0);
					bas = doNcotizacion ? 100000 : 10000;
					primaTotalInput.setEnabled(false);
				} else {
					pago = values.get(2);
					pagoSpinner.setSelection(2);
					primaTotalInput.setEnabled(true);
					primaTotalInput.setText("500");
				}
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		pagoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				pago = (ValueVO<Integer>) parent.getItemAtPosition(pos);
			
				
				primaTotalT.setText("Prima Total "
						+ parent.getItemAtPosition(pos) + ":");
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		coberturasRadio
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radCP:
							CotizadorUtil.getInstance().setDoPFT( true );
							break;
						case R.id.radSP:
							CotizadorUtil.getInstance().setDoPFT( false );
							break;
						default:
							break;
						}
						refresh();
					}
				});

		cotizacionRadio
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radNP:
							doNcotizacion = true;
							dobas = true;
							break;
						case R.id.radII:
							doNcotizacion = false;
							break;
						default:
							break;
						}
						refresh();
					}
				});
	}

	private void pageCobertura() {

		basCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dobas = ((CheckBox) v).isChecked();				
				bas = dobas ? (doNcotizacion ? bas : 10000) : 0;
				basSumaEdit.setEnabled(dobas);
				refresh();
			}
		});

		ciiCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docii = ((CheckBox) v).isChecked();
				cii = docii ? (doNcotizacion ? bas : 10000) : 0;
				ciiSumaEdit.setEnabled(docii);
				refresh();
			}
		});

		cmaCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docma = ((CheckBox) v).isChecked();				
				if (docma) {
					cma = doNcotizacion ? bas : 10000;
					dotiba = false;
				} else {
					cma = 0;
				}
				cmaSumaEdit.setEnabled(docma);
				refresh();
			}
		});

		tibaCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dotiba = ((CheckBox) v).isChecked();				
				if (dotiba) {
					tiba = doNcotizacion ? bas : 10000;
					docma = false;
				} else {
					tiba = 0;
				}
				tibaSumaEdit.setEnabled(dotiba);
				refresh();
			}
		});

		catCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docat = ((CheckBox) v).isChecked();				
				cat = docat ? doNcotizacion ? bas : 10000 : 0;
				catSumaEdit.setEnabled(docat);
				refresh();
			}
		});

		gfaCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dogfa = ((CheckBox) v).isChecked();				
				gfa = dogfa ? (doNcotizacion ? (int)(bas * .35) : 10000) : 0;
				gfaSumaEdit.setEnabled(dogfa);
				refresh();
			}
		});

		geCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doge = ((CheckBox) v).isChecked();				
				ge = doge ? doNcotizacion ? bas : 10000 : 0;
				geSumaEdit.setEnabled(doge);
				refresh();
			}
		});

		bitCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dobit = ((CheckBox) v).isChecked();				
				refresh();
			}
		});

	}

	private void pageconyuge() {
		conyugeCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doconyuge = ((CheckBox) v).isChecked();				
				if (!doconyuge) {
					dobacy = doconyuge;
					dogfc = doconyuge;
					dogpc = doconyuge;
				}
				refresh();
			}
		});

		bacyCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dobacy = ((CheckBox) v).isChecked();				
				bacy = dobacy ? doNcotizacion ? bas : 10000 : 0;
				bacySumaEdit.setEnabled(dobacy);
				refresh();
			}
		});

		gfcCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dogfc = ((CheckBox) v).isChecked();				
				gfc = dogfc ? (int) (doNcotizacion ? (int)(bas * .35) : 10000) : 0;
				gfcSumaEdit.setEnabled(dogfc);
				refresh();
			}
		});

		gpcCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dogpc = ((CheckBox) v).isChecked();				
				if (dogpc) {
					gpc = doNcotizacion ? bas : 10000;
					estadosCP = prenderBandera(estadosCP, ENABLE_GPC);
				} else {
					gpc = 0;
					estadosCP = apagarBandera(estadosCP, ENABLE_GPC);
				}
				gpcSumaEdit.setEnabled(dogpc);
				refresh();
			}
		});

	}

	private void pageHijos() {
		ccompCheck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doccomp = ((CheckBox) v).isChecked();				
				if (doccomp)
					ccomp = doNcotizacion ? bas : 10000;
				ccompSumaEdit.setEnabled(doccomp);
				ccompEdadInput.setEnabled(doccomp);
				refresh();
			}
		});

		catcomp1Check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docatcomp1 = ((CheckBox) v).isChecked();				
				if (docatcomp1) {
					catcomp1 = doNcotizacion ? bas : 10000;
					estadosCP = prenderBandera(estadosCP, ENABLE_CAT1);
				} else {
					catcomp1 = 0;
					estadosCP = apagarBandera(estadosCP, ENABLE_CAT1);
				}

				catcomp1SumaEdit.setEnabled(docatcomp1);
				catcomp1EdadInput.setEnabled(docatcomp1);
				refresh();
			}
		});

		catcomp2Check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docatcomp2 = ((CheckBox) v).isChecked();				
				if (docatcomp2) {
					catcomp2 = doNcotizacion ? bas : 10000;
					estadosCP = prenderBandera(estadosCP, ENABLE_CAT2);
				} else {
					catcomp2 = 0;
					estadosCP = apagarBandera(estadosCP, ENABLE_CAT2);
				}
				catcomp2SumaEdit.setEnabled(docatcomp2);
				catcomp2EdadInput.setEnabled(docatcomp2);
				refresh();
			}
		});

		catcomp3Check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				docatcomp3 = ((CheckBox) v).isChecked();				
				if (docatcomp3) {
					estadosCP = prenderBandera(estadosCP, ENABLE_CAT3);
					catcomp3 = doNcotizacion ? bas : 10000;
				} else {
					catcomp3 = 0;
					estadosCP = apagarBandera(estadosCP, ENABLE_CAT3);
				}

				catcomp3SumaEdit.setEnabled(docatcomp3);
				catcomp3EdadInput.setEnabled(docatcomp3);
				refresh();
			}
		});

		gfhSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectedGFH = (ValueVO<Double>) parent.getItemAtPosition(pos);
				dogfh = (selectedGFH.getValue().floatValue() > 0) ? true : false;
				nHijos = pos;
				if (selectedGFH.getValue().floatValue() > 0 ) {
					gfh = doNcotizacion ? (int)(bas * .35) : 10000;
				}
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ccompSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				catcompsexo = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
		});
		
		catcomp1Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				catcomp1sexo = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		catcomp2Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				catcomp2sexo = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		catcomp3Spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				catcomp3sexo = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void setUpProfesiones() {
		List<ProfesionVO> profesiones;

		CommonDAO<ProfesionVO> polizaDS = new CommonDAO<ProfesionVO>(caller,
				ProfesionVO.class, SipacOpenHelper.TABLE_PROFESIONES);
		polizaDS.open();
		profesiones = polizaDS.getAllItems();
		polizaDS.close();
		profesiones.add(0, new ProfesionVO("Ninguno", 0, 1, 1));
		dialog.setProfesionesData(profesiones);
	}

	@SuppressLint("UseValueOf")
	private void setUpHijos() {
		ArrayList<ValueVO<Double>> values = new ArrayList<ValueVO<Double>>();
		values.add(new ValueVO<Double>(new Double(0.0),
				"Sin Gastos Funerarios Hijos"));
		values.add(new ValueVO<Double>(new Double(1.360),
				"(GFH): 1 Hijo"));
		values.add(new ValueVO<Double>(new Double(2.760),
				"(GFH): 2 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"(GFH): 3 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"(GFH): 4 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"(GFH): 5 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"(GFH): 6 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"(GFH): 7 Hijos"));

		ArrayAdapter<ValueVO<Double>> dataAdapter = new ArrayAdapter<ValueVO<Double>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gfhSpinner.setAdapter(dataAdapter);
		selectedGFH = values.get(0);

	}

	

	private void setUpPago() {

		values.add(new ValueVO<Integer>(1, "Anual"));
		values.add(new ValueVO<Integer>(12, "Mensual"));
		values.add(new ValueVO<Integer>(24, "Quincenal"));
		ArrayAdapter<ValueVO<Integer>> dataAdapter = new ArrayAdapter<ValueVO<Integer>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		pagoSpinner.setAdapter(dataAdapter);

		pago = values.get(0);

	}

	private void setUpCalculo() {
		ArrayList<ValueVO<Boolean>> values = new ArrayList<ValueVO<Boolean>>();
		values.add(new ValueVO<Boolean>(true, "Suma Asegurada"));
		values.add(new ValueVO<Boolean>(false, "Prima Anual"));
		ArrayAdapter<ValueVO<Boolean>> dataAdapter = new ArrayAdapter<ValueVO<Boolean>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		calculoSpiner.setAdapter(dataAdapter);

		calculo = values.get(0);
	}

	private void setUpSexo() {
		ArrayList<ValueVO<Integer>> values = new ArrayList<ValueVO<Integer>>();
		values.add(new ValueVO<Integer>(CotizadorUtil.MASCULINO, "M"));
		values.add(new ValueVO<Integer>(CotizadorUtil.FEMENINO, "F"));
		ArrayAdapter<ValueVO<Integer>> dataAdapter = new ArrayAdapter<ValueVO<Integer>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ccompSpinner.setAdapter(dataAdapter);
		catcomp1Spinner.setAdapter(dataAdapter);
		catcomp2Spinner.setAdapter(dataAdapter);
		catcomp3Spinner.setAdapter(dataAdapter);

		catcomp1sexo = values.get(0);
		catcomp2sexo = values.get(0);
		catcomp3sexo = values.get(0);
	}
	
	private double getSumaAsegurada(EdadVO pEdad, ProfesionVO pProfesion, EdadVO pEdadConyuge,
								   double pPrimaTotal, int pSexo, EdadVO pEdadReal, EdadVO pEdadCom,
								   EdadVO pEdadCom1, EdadVO pEdadCom2, EdadVO pEdadCom3, 
								   int pSexoC1, int pSexoC2, int pSexoC3) throws GEOmmittedError, OverFlowError {
		double numerador = 0;
		double denominador = 0;
		do{
			numerador = primaTotalTem * 1000;
			denominador = (dobas ? CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion) : 0)
							+ (docii ? CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion) : 0)
							+ (docma ? CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion) : 0)
							+ (dotiba ? CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion) : 0)
							+ ( docat ? CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) : 0 )
							+ ( dogfa ? (double)(CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35 : 1)) : 0)
							+ ( doge ? CotizadorUtil.getInstance().getGEFactor(pEdad, pProfesion) : 0 )
							+ ( dobacy ? (double)(CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (dogfc ? .65 : 1)): 0 )
							+ ( dogfc ? (double)(CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * .35) : 0 )
							+ ( dogpc ? CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO) : 0 )
							+ (double)(CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * (.35))
							+ (doccomp ? CotizadorUtil.getInstance().getBACYFactor(pEdadCom) : 0 )
							+ ( docatcomp1 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexoC1) : 0 )
							+ ( docatcomp2 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexoC2) : 0 )
							+ ( docatcomp3 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexoC3) : 0 ) 
							;
			if( dobit ){
				denominador += ((dobas ? CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion) : 0)
								+ ( docat ? CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) : 0 )
								+ ( dogfa ? (double)(CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35 : 1)) : 0)
								+ ( doge ? CotizadorUtil.getInstance().getGEFactor(pEdad, pProfesion) : 0 )
								+ ( dobacy ? (double)(CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) ) * (dogfc ? .65 : 1): 0 )
								+ ( dogfc ? (double)(CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (.35) ): 0 )
								+ ( dogpc ? CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO) : 0 )
								+ (double)(CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * ( .35 ))
								+ (doccomp ? CotizadorUtil.getInstance().getBACYFactor(pEdadCom) : 0 )
								+ ( docatcomp1 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexoC1) : 0 )
								+ ( docatcomp2 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexoC2) : 0 )
								+ ( docatcomp3 ? CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexoC3) : 0 ) )
								* factorBIT(pEdad, pProfesion);
			}
			
			discounts.reset();
			if(dobas && discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BAS ) ){
				if(discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BAS ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion) * discounts.getLowerLimit() );
					denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion);	
				}
				discounts.hacerDescuento(Descuentos.OVERFLOW_BAS);
			}
			
			if(docat && (discounts.isActivoOverflow( Descuentos.OVERFLOW_CAT ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CAT ))){
				if(discounts.isActivoOverflow( Descuentos.OVERFLOW_CAT )){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) * discounts.getLimiteCAT() );
				}else if(discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CAT ) && doNcotizacion ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo);
				discounts.hacerDescuento(Descuentos.OVERFLOW_CAT);
			}
			
			if( dogfa && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFA ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFA ) ) ){
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFA ) ){
					numerador -=  ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion)  * discounts.getLimiteGFA() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFA ) ){
					numerador -=  ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion)  * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35 : 1));
				discounts.hacerDescuento(Descuentos.OVERFLOW_GFA);
				
			}
			
			if( docii && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CII ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CII ) )){
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CII ) ){
					numerador -= (CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion) * discounts.getLimiteCII() );			
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CII ) ){
					numerador -= (CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion) * discounts.getLowerLimit() );					
				}
				denominador -= CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion);	
				discounts.hacerDescuento(Descuentos.OVERFLOW_CII);
			}
			
			if( docma && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CMA ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CMA ) ) ) {
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CMA ) ){
					numerador -= (CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion) * discounts.getLimiteCMA() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CMA ) ){
					numerador -= (CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion) * discounts.getLowerLimit() );							
				}
				denominador -= CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion);		
				discounts.hacerDescuento(Descuentos.OVERFLOW_CMA);
			}
			
			if( dotiba && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_TIBA ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_TIBA ) )){
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_TIBA ) ){
					numerador -= (CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion) * discounts.getLimiteTIBA() );					
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_TIBA ) ){
					numerador -= (CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion) * discounts.getLowerLimit() );
				}
				denominador -= CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion);
				discounts.hacerDescuento(Descuentos.OVERFLOW_TIBA);
			}
			
			if( dobacy && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_BACY ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BACY ) ) ){
				if ( discounts.isActivoOverflow( Descuentos.OVERFLOW_BACY ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteBACY() );				
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BACY ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteBACY() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (dogfc ? .65 : 1));
				discounts.hacerDescuento(Descuentos.OVERFLOW_BACY);
			}
			
			if( dogfc && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFC ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFC ) ) ){
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFC ) ){
					numerador -= (( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteGFC() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFC ) ){
					numerador -= (( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (.35) );
				discounts.hacerDescuento(Descuentos.OVERFLOW_GFC);
			}
			
			if( dogfh && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFH ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFH ) ) ){ 
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_GFH ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * discounts.getLimiteGFH();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFH ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * discounts.getLowerLimit();
				}				
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * (.35));
				discounts.hacerDescuento(Descuentos.OVERFLOW_GFH);
			}
			
			if( dogpc && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATC ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATC ) ) ){ 
				if ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATC ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ((CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO)) * discounts.getLimiteCATC() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATC ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ((CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO)) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO));
				discounts.hacerDescuento(Descuentos.OVERFLOW_CATC);
			}
			
			if( doccomp && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CCOM ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CCOM ) ) ) {
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CCOM ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ( CotizadorUtil.getInstance().getBACYFactor(pEdadCom) * discounts.getLimiteCCOM() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CCOM ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ( CotizadorUtil.getInstance().getBACYFactor(pEdadCom) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadCom);
				discounts.hacerDescuento(Descuentos.OVERFLOW_CCOM);
			}
			
			if( docatcomp1 && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM1 ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM1 ) ) ){ 
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM1 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo) * discounts.getLimiteCATCOM1() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM1 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo);
				discounts.hacerDescuento(Descuentos.OVERFLOW_CATCOM1);
			}
			
			if( docatcomp2 && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM2 ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM2 ) ) ){
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM2 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo) * discounts.getLimiteCATCOM2() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM2 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo) * discounts.getLowerLimit() );
				}				
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo);
				discounts.hacerDescuento(Descuentos.OVERFLOW_CATCOM2);
			}
			
			if( docatcomp3 && ( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM3 ) || discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM3 ) ) ){ 
				if( discounts.isActivoOverflow( Descuentos.OVERFLOW_CATCOM3 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo) * discounts.getLimiteCATCOM3() );
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM3 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo);
				discounts.hacerDescuento(Descuentos.OVERFLOW_CATCOM3);
			}
			
			discounts.checkDescuentos(numerador / denominador);
			discounts.checkLowerDescuentos(numerador / denominador);
			if( (numerador/denominador > 100000000) ){
				throw new OverFlowError();
			}
			try{
				if( (edad < 18 && ((long)(numerador/denominador) > 3000000)) || (edad >= 18 && ((long)(numerador/denominador) > 100000000))  ){
					Toast.makeText(caller, "No se encontro una solución.", Toast.LENGTH_SHORT ).show();
					primaTotalInput.setTextColor( Color.RED );
					return 0.0;
				}
				
			}catch (Exception e) {
				Toast.makeText(caller, "Esta pasando algo raro.", Toast.LENGTH_SHORT ).show();
			}
			
		}while( !discounts.isValid() );
		if( (edad < 18 && ( 
				(dobas ? numerador/denominador : 0) + 
				(doge ? numerador/denominador : 0) + 
				(dogfa ? Math.min(numerador/denominador * .35, 180000) : 0) ) > MAX) ){
			Toast.makeText(caller, "La suma de BAS + GE + GFA pasan los 3'000,000.00.", Toast.LENGTH_LONG ).show();
			primaTotalInput.setTextColor( Color.RED );
			return 0.0;
		}
		primaTotalInput.setTextColor( Color.BLACK );
		return numerador/denominador;
	}

	
	
	private EdadVO getEdadCalculoVO(int pEdad, int pSexo, boolean pFuma) {
		try {
			List<EdadVO> edades;
			EdadVO selectedEdadVO = null;
			int searchEdad = pEdad;
			int resta;
			if (pSexo == CotizadorUtil.FEMENINO)
				resta = 3;
			else
				resta = 0;
			if (pFuma)
				resta += 0;
			else
				resta += 2;

			searchEdad -= resta;
			searchEdad = Math.max(searchEdad, 15);

			CommonDAO<EdadVO> polizaDS = new CommonDAO<EdadVO>(caller,
					EdadVO.class, SipacOpenHelper.TABLE_EDADES);

			polizaDS.open();
			edades = polizaDS.getFilteredItems("edad = " + searchEdad);

			polizaDS.close();

			if (!edades.isEmpty()) {
				selectedEdadVO = edades.get(0);
			}
			return selectedEdadVO;
		} catch (Exception e) {
			Log.i("error", "" + e);
		}
		return null;

	}

	private EdadVO getEdadVO(int pEdad) {
		List<EdadVO> edades;
		EdadVO selectedEdadVO = null;
		CommonDAO<EdadVO> polizaDS = new CommonDAO<EdadVO>(caller,
				EdadVO.class, SipacOpenHelper.TABLE_EDADES);
		polizaDS.open();
		edades = polizaDS.getFilteredItems("edad = " + pEdad);
		polizaDS.close();

		if (!edades.isEmpty()) {
			selectedEdadVO = edades.get(0);
		}
		return selectedEdadVO;
	}
	
	@Override
	public void refresh(int sexo, int edad, boolean _fuma, ProfesionVO ocupacion, String fullName) {
		this.sexo = sexo;
		this.edad = (edad < 15 ? 15 : Math.min(edad, 70));
		this._fuma = _fuma;
		this.selectedProfesion = ocupacion;
		this.fullName = fullName;
		discounts.setLimiteCII( edad < 18 ? 3000000 : 6000000 );
		discounts.setLimiteCMA( edad < 18 ? 3000000 : 6000000 );
		discounts.setLimiteTIBA( edad < 18 ? 3000000 : 6000000 );
		refresh();
	}

	private void refresh() {
		double auxSUmAsegurada;
		primaTotal = 0;
		bit = 0;
		MAX = (edad < 18) ? 3000000 : 100000000;

		basHandler.setMaxRange(MAX);
		primaTotalHandler.setMaxRange(MAX);
		if ( doNcotizacion && calculo.getValue() ) {
			bas = Math.max(30000, Math.min( bas, MAX ));			
			geHandler.setMaxRange(bas);
			ciiHandler.setMaxRange(bas);
			cmaHandler.setMaxRange(bas);
			tibaHandler.setMaxRange(bas);
			catHandler.setMaxRange( Math.min(1000000.0, bas) );
			gfaHandler.setMaxRange(Math.min(180000.0, bas));
			gfcHandler.setMaxRange(Math.min(180000.0, bas));
			bacyHandler.setMaxRange( Math.min(bas, 344760) );
			catcomp1Handler.setMaxRange( Math.min(1000000.0, bas) );
			catcomp2Handler.setMaxRange( Math.min(1000000.0, bas) );
			catcomp3Handler.setMaxRange( Math.min(1000000.0, bas) );
			gfhHandler.setMaxRange( Math.min(180000.0, bas) );
			gpcHandler.setMaxRange( Math.min(1000000.0, bas) );
			ccompHandler.setMaxRange( ccompEdad < 18 ? Math.min(3000000, bas) : bas );

			gfh = dogfh ? Math.max(34476, Math.min(gfh, (int) (bas * .35))) : 0;
			gfc = dogfc ? Math.max(34476, Math.min(gfc, (int) (bas * .35))) : 0;
			gfa = dogfa ? Math.max(34476, Math.min(gfa, (int) (bas * .35))) : 0;

			cat = Math.max(30000, cat);
			catcomp1 = Math.max(30000, catcomp1);
			catcomp2 = Math.max(30000, catcomp2);
			catcomp3 = Math.max(30000, catcomp3);
			
			basHandler.setMinRange(MIN);
			ciiHandler.setMinRange(MIN);
			cmaHandler.setMinRange(MIN);
			tibaHandler.setMinRange(MIN);
			catHandler.setMinRange(MIN);
			gfaHandler.setMinRange(MIN);
			geHandler.setMinRange(MINGE);
			ccompHandler.setMinRange(MIN);
			gfcHandler.setMinRange(MIN);
			bacyHandler.setMinRange(MIN);
			catcomp1Handler.setMinRange(MIN);
			catcomp2Handler.setMinRange(MIN);
			catcomp3Handler.setMinRange(MIN);
			gfhHandler.setMinRange(MIN);
			gpcHandler.setMinRange(MIN);			
		} else {
			ciiHandler.setMaxRange(MAX);
			cmaHandler.setMaxRange(MAX);
			tibaHandler.setMaxRange(MAX);
			catHandler.setMaxRange(1000000.0);
			gfaHandler.setMaxRange( 180000.0 );
			geHandler.setMaxRange(MAX);
			ccompHandler.setMaxRange( ccompEdad < 18 ? 3000000 : 100000000 );
			gfcHandler.setMaxRange( 180000 );
			bacyHandler.setMaxRange(344760);
			catcomp1Handler.setMaxRange( 1000000.0 );
			catcomp2Handler.setMaxRange( 1000000.0 );
			catcomp3Handler.setMaxRange( 1000000.0 );
			gfhHandler.setMaxRange( 180000.0 );
			gpcHandler.setMaxRange( 1000000.0 );
			
			basHandler.setMinRange(0);
			ciiHandler.setMinRange(0);
			cmaHandler.setMinRange(0);
			tibaHandler.setMinRange(0);
			catHandler.setMinRange(0);
			gfaHandler.setMinRange(0);
			geHandler.setMinRange(0);
			ccompHandler.setMinRange(0);
			gfcHandler.setMinRange(0);
			bacyHandler.setMinRange(0);
			catcomp1Handler.setMinRange(0);
			catcomp2Handler.setMinRange(0);
			catcomp3Handler.setMinRange(0);
			gfhHandler.setMinRange(0);
			gpcHandler.setMinRange(0);
		}
		
		if( bas < 98503 && doNcotizacion ){
			gfaCheck.setChecked( dogfa = false );
			gfcCheck.setChecked( dogfc = false );
			gfhSpinner.setSelection( 0 );
			selectedGFH = (ValueVO<Double>) gfhSpinner.getItemAtPosition(0);
			dogfh = false;
		}
		
//		if ((bas + gfa + ge) > MAX) {			
//			if (doge && dogfa) {
//				if( dobas ){
//					gfa = Math.min(180000, gfa);
//					ge = (MAX - gfa) / 2;
//					bas = (MAX - gfa) / 2;
//				}else{
//					gfa = Math.min(180000, gfa);
//					ge = (MAX - gfa);
//				}
//				
//			} else if (doge && dobas) {
//					ge = (MAX / 2);
//					bas = (MAX / 2);
//			} else if (dogfa) {
//				gfa = Math.min(180000, gfa);
//				bas = (MAX - gfa);
//			}
//TODO:		}
		
		if( doNcotizacion ){
			tiba = dotiba ? Math.max(30000, Math.min(bas, tiba) ) : 0;
			cma = docma ? Math.max(30000,  Math.min(bas, cma) ) : 0;
			cii = docii ?  Math.max(30000,  Math.min(bas, cii) ) : 0;
			cat = docat ? Math.max(30000,  Math.min(bas, cat) ) : 0;
			ge = doge ? Math.min(bas, ge) : 0;
			gpc = dogpc ? Math.min(1000000, Math.max(30000, Math.min(bas, gpc) ) ) : 0;
			ccomp = doccomp ? Math.max(30000,  Math.min(bas, ccomp) ) : 0;
			bacy = dobacy ? Math.max( 30000,  Math.min(bas, bacy) ) : 0;
		}
		
		bacy = dobacy ? Math.min( doNcotizacion ? Math.min(bas, 344760) : 344760, bacy) : 0;
		
		tiba = dotiba ? Math.min( ( edad < 18 ? 3000000 : 6000000 ), tiba) : 0;
		cma = docma ? Math.min( ( edad < 18 ? 3000000 : 6000000 ), cma) : 0;
		cii = docii ? Math.min( ( edad < 18 ? 3000000 : 6000000 ), cii) : 0;
		
		cat = docat ? Math.min(1000000, cat) : 0;		
		gpc = dogpc ? Math.min(1000000, gpc) : 0;
		
		ccomp = doccomp ? Math.min( (ccompEdad < 18 ? 3000000 : 100000000), ccomp ) : 0;
		catcomp1 = docatcomp1 ? Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp1) : catcomp1) : 0;
		catcomp2 = docatcomp2 ? Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp2) : catcomp2) : 0;
		catcomp3 = docatcomp3 ? Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp3) : catcomp3) : 0;
		
		gfh = dogfh ? Math.min(180000, doNcotizacion ? Math.min(bas, gfh) : gfh) : 0;
		gfc = dogfc ? Math.min(180000, doNcotizacion ? Math.min(bas, gfc) : gfc) : 0;
		gfa = dogfa ? Math.min(180000, doNcotizacion ? Math.min(bas, gfa) : gfa) : 0;

		if ((bacy + gfc) > bas && dobacy && dogfc &&  doNcotizacion) {
			gfc = doNcotizacion ? (int) (bas * .35) : gfc;
			gfc = Math.min(180000, gfc);
			bacy = Math.min(344760, (bas - gfc));
		}
		
		if( edad < 18 )
			if ((bas + gfa + ge) > MAX) {			
				if (dogfa) {
					gfa = dogfa?Math.min(180000, gfa):0;
				}			
				if (doge){
					ge = doge?((MAX - gfa)/Math.max(1,(doge?1:0)+(dobas?1:0))):0;
				}			
				if(dobas){
					bas = dobas?((MAX - gfa)/Math.max(1,(doge?1:0)+(dobas?1:0))):0;
				}
			}
		
		EdadVO edadcalculoVO = getEdadCalculoVO(edad, sexo, _fuma);
		EdadVO edadVO = getEdadVO(edad);
		EdadVO edadConyugeVO = getEdadVO(edadConyugue);

		if( dobas || dogfa || docat || docatcomp1 || docatcomp2 || docatcomp3 || dogpc ){
			bitCheck.setEnabled(true);
		}else{
			dobit = false;
			bitCheck.setChecked(dobit);
			bitCheck.setEnabled(dobit);			
		}
		
		doedadcii = (edad > 55) ? (docii = false) : true;
		doedadbit = (edad > 55) ? (dobit = false) : true;
		doedadcma = (edad > 65) ? (docma = false) : true;
		doedadtiba = (edad > 65) ? (dotiba = false) : true;
		doedadcat = (edad > 65) ? (docat = false) : true;
		doedadgpc = (edadConyugue > 65) ? (dogpc = false) : true;
		doedadcatcomp1 = (catcomp1Edad > 65) ? (docatcomp1 = false) : true;
		doedadcatcomp2 = (catcomp2Edad > 65) ? (docatcomp2 = false) : true;
		doedadcatcomp3 = (catcomp3Edad > 65) ? (docatcomp3 = false) : true;

		if( selectedProfesion.getMillar() == 0 && selectedProfesion.getAccidente() == 1 && selectedProfesion.getInvalidez() == 1 ){
			extraPrimas.setText("No Hay ExtraPrimas");
		}else{
		
			extraPrimas.setText("Vida: "
					+ (selectedProfesion.getMillar() == -1 ? "Rechazado."
							: + selectedProfesion.getMillar() + " millar.")
					+ "\nAccidentes: "
					+ (selectedProfesion.getAccidente() == -1 ? "Rechazado."
							: + selectedProfesion.getAccidente() + " tantos.")
					+ "\nInvalidez: "
					+ (selectedProfesion.getInvalidez() == -1 ? "Rechazado."
							: + selectedProfesion.getInvalidez() + " tantos."));
		}
		conyugeAge.setEnabled( doconyuge );
		
		conyugeAge.setText("" + edadConyugue);
		ccompEdadInput.setText("" + ccompEdad);
		catcomp1EdadInput.setText("" + catcomp1Edad);
		catcomp2EdadInput.setText("" + catcomp2Edad);
		catcomp3EdadInput.setText("" + catcomp3Edad);
		
		if (selectedProfesion.getMillar() == -1) {
			bloquearTodo();
		} else {
			if (!doNcotizacion) {
				basHandler.setMinRange(0);
			}else{
				basCheck.setChecked( true );
			}
			
			dobas = basCheck.isChecked();
			basCheck.setEnabled(!doNcotizacion);
			bitCheck.setEnabled(doedadbit);
			ciiCheck.setEnabled(doedadcii);
			cmaCheck.setEnabled(doedadcma);
			tibaCheck.setEnabled(doedadtiba);
			catCheck.setEnabled(doedadcat);
			gpcCheck.setEnabled(doedadgpc);
			ccompCheck.setEnabled(true);
			conyugeCheck.setEnabled(true);

			ciiSumaEdit.setEnabled(doedadcii);
			cmaSumaEdit.setEnabled(doedadcma);
			tibaSumaEdit.setEnabled(doedadtiba);
			catSumaEdit.setEnabled(doedadcat);
			gpcSumaEdit.setEnabled(doedadgpc);

			gfhSumaEdit.setEnabled( dogfh );
			catcomp1Check.setEnabled(doedadcatcomp1);
			catcomp2Check.setEnabled(doedadcatcomp2);
			catcomp3Check.setEnabled(doedadcatcomp3);
			bacyCheck.setEnabled(doconyuge);
			gfcCheck.setEnabled(doconyuge);
			gpcCheck.setEnabled(doconyuge && (estadosCP != 7));
			geCheck.setEnabled(true);
			catcomp1Check.setEnabled(estadosCP != 11);
			catcomp2Check.setEnabled(estadosCP != 13);
			catcomp3Check.setEnabled(estadosCP != 14);
			
			ccompSpinner.setEnabled( doccomp );
			catcomp1Spinner.setEnabled( docatcomp1 );
			catcomp2Spinner.setEnabled( docatcomp2 );
			catcomp3Spinner.setEnabled( docatcomp3 );

			bitCheck.setChecked(dobit);
			ciiCheck.setChecked(docii);
			cmaCheck.setChecked(docma);
			tibaCheck.setChecked(dotiba);
			catCheck.setChecked(docat);
			gfaCheck.setChecked(dogfa);
			geCheck.setChecked(doge);
			gfcCheck.setChecked(dogfc);
			gpcCheck.setChecked(dogpc);
			conyugeCheck.setChecked(doconyuge);
			ccompCheck.setChecked(doccomp);
			catcomp1Check.setChecked(docatcomp1);
			catcomp2Check.setChecked(docatcomp2);
			catcomp3Check.setChecked(docatcomp3);
			bacyCheck.setChecked(dobacy & doconyuge);

			if (selectedProfesion.getAccidente() == -1) {
				cmaCheck.setEnabled(docma = false);
				tibaCheck.setEnabled(dotiba = false);
			}
			if (selectedProfesion.getInvalidez() == -1) {
				bitCheck.setEnabled(dobit = false);
				ciiCheck.setEnabled(docii = false);
			}
			
			porSumaAsegurada(calculo.getValue());

			if (!calculo.getValue()) {
				if( !primaTotalInput.getText().toString().equals("") ){
					primaTotal = (Double.parseDouble(primaTotalInput.getText()
							.toString()) * pago.getValue().intValue()) ;
				}else{
					primaTotal = 0;
				}
				
				primaTotal = Math.max(0, primaTotal - primaExcedente);
				primaTotalTem = primaTotal;
				
				discounts = edad < 18 ? new Descuentos(1000000, 180000, 344760, 180000, 1000000, 180000, 
						MAX, 1000000, 1000000, 1000000, MAX, MAX, MAX, MAX, MAX) 
				: new Descuentos(1000000, 180000, 344760, 180000, 1000000, 180000, 3000000, 1000000, 
						1000000, 1000000, 6000000, 6000000, 6000000, 6000000, 6000000);

				discounts.resetAll();
				try{
					auxSUmAsegurada = getSumaAsegurada(edadcalculoVO, selectedProfesion, edadConyugeVO, primaTotal, 
													sexo, edadVO, getEdadVO(ccompEdad), getEdadVO(catcomp1Edad), 
													getEdadVO(catcomp2Edad), getEdadVO(catcomp3Edad),
													catcomp1sexo.getValue().intValue(),
													catcomp2sexo.getValue().intValue(), catcomp3sexo.getValue().intValue());
				//TODO:
				}catch (GEOmmittedError error){
					auxSUmAsegurada = 0;
					Toast.makeText(caller, "Error al calcular.", Toast.LENGTH_LONG ).show();
				}catch( OverFlowError error){
					auxSUmAsegurada = 0;
					Toast.makeText(caller, "Calculos fuera de limites.", Toast.LENGTH_LONG ).show();
				}
				if ( discounts.isActivoOverflow(Descuentos.OVERFLOW_CAT) ) {
					cat = discounts.getLimiteCAT();
				} else if(discounts.isActivoLowerOverflow(Descuentos.OVERFLOW_CAT)){
					cat = discounts.getLowerLimit();
				}
				else{
					cat = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CII) ){
					cii = discounts.getLimiteCII();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CII ) ){
					cii = discounts.getLowerLimit();
				}else{
					cii = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CMA) ){
					cma = discounts.getLimiteCMA();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CMA ) ){
					cma = discounts.getLowerLimit();
				}else{
					cma = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_TIBA) ){
					tiba = discounts.getLimiteTIBA();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_TIBA ) ){
					tiba = discounts.getLowerLimit();
				}else{
					tiba = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_GFA) ) {
					gfa = discounts.getLimiteGFA();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFA ) ){
					gfa = discounts.getLowerLimit();
				} else {
					gfa = (double)(auxSUmAsegurada * (doNcotizacion ? .35 : 1) );
				}				
				
				ge = auxSUmAsegurada;
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_BACY) ){
					bacy = discounts.getLimiteBACY();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BACY ) ){
					bacy = discounts.getLowerLimit();
				}else{
					bacy = (double)(auxSUmAsegurada * (dogfc ? .65 : 1));
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_GFC) ){
					gfc = discounts.getLimiteGFC();		
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFC ) ){
					gfc = discounts.getLowerLimit();
				}else{
					gfc = (double)(auxSUmAsegurada * .35);
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_GFH) ){
					gfh = discounts.getLimiteGFH();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_GFH ) ){
					gfh = discounts.getLowerLimit();
				}else{
					gfh = (double)(auxSUmAsegurada * .35);
				}

				if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_BAS ) ){
					bas = discounts.getLowerLimit();
				}else{
					bas = auxSUmAsegurada;
				}
				
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CATC) ){
					gpc = discounts.getLimiteCATC();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATC ) ){
					gpc = discounts.getLowerLimit();
				}else{
					gpc = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CCOM) ){
					ccomp = discounts.getLimiteCCOM();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CCOM ) ){
					ccomp = discounts.getLowerLimit();
				}else{
					ccomp = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CATCOM1) ){
					catcomp1 = discounts.getLimiteCATCOM1();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM1 ) ){
					catcomp1 = discounts.getLowerLimit();
				}else{
					catcomp1 = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CATCOM2) ){
					catcomp2 = discounts.getLimiteCATCOM2();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM2 ) ){
					catcomp2 = discounts.getLowerLimit();
				}else{
					catcomp2 = auxSUmAsegurada;
				}
				
				if( discounts.isActivoOverflow(Descuentos.OVERFLOW_CATCOM3) ){
					catcomp3 = discounts.getLimiteCATCOM3();
				}else if( discounts.isActivoLowerOverflow( Descuentos.OVERFLOW_CATCOM3 ) ){
					catcomp3 = discounts.getLowerLimit();
				}else{
					catcomp3 = auxSUmAsegurada;
				}
				if((cii <= 0 && docii) || 
						   (cat <= 0 && docat) || 
						   (cma <= 0 && docma) || 
						   (tiba <= 0 && dotiba) || 
						   (gfa <= 0 && dogfa) || 
						   (bacy <= 0 && dobacy)|| 
						   (ge <= 0 && doge) || 
						   (gfc <= 0 && dogfc) || 
						   (gfh <= 0 && dogfh) || 
						   (bas <= 0 && dobas) || 
						   (gpc <= 0 && dogpc) || 
						   (ccomp <= 0 && doccomp) || 
						   (catcomp1 <= 0 && docatcomp1)|| 
						   (catcomp2 <= 0 && docatcomp2) || 
						   (catcomp3 <= 0 && docatcomp3))
						{
							caller.getMessageBuilder().setMessage("No  se puede calcular una cotización. Por favor deseleccione algunos beneficios o aumente el valor de la prima anual");
							caller.getMessageBuilder().create().show();
							return;
						}
				if( dobit ){
			
					bit += (dobas ? CotizadorUtil.getInstance().getBAS(edadcalculoVO, selectedProfesion, bas)
							: 0)
							+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO, cat, sexo) : 0)
							+ (dogfa ? CotizadorUtil.getInstance().getGFA(edadcalculoVO, selectedProfesion, gfa)
									: 0)
							+ (doge ? CotizadorUtil.getInstance().getGE(edadcalculoVO, selectedProfesion, ge)
									: 0)
							+ (dobacy ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, bacy) : 0)
							+ (dogfc ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, gfc) : 0)
							+ (dogpc ?CotizadorUtil.getInstance(). getCAT(edadConyugeVO, gpc,
									((sexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO))
									: 0)
							+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
							+ (doccomp ? CotizadorUtil.getInstance().getBACY(getEdadVO(ccompEdad), ccomp) : 0)
							+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp1Edad),
									catcomp1, catcomp1sexo.getValue().intValue())
									: 0)
							+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp2Edad),
									catcomp2, catcomp2sexo.getValue().intValue())
									: 0)
							+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp3Edad),
									catcomp3, catcomp3sexo.getValue().intValue())
									: 0);

					bit *= factorBIT(edadcalculoVO, selectedProfesion);
				}
				if( ((dogfa && gfa < 34476) || (dogfc && gfc < 34476) || (dogfh && gfh < 34476)) && doNcotizacion ){
					Toast.makeText(caller, "Error al calcular. Por favor verifique los beneficios funerarios", Toast.LENGTH_LONG ).show();
					refreshCotizador();
					return;
				}
			} else {
				primaTotalInput.setEnabled(false);
				bit += (dobas ? CotizadorUtil.getInstance().getBAS(edadcalculoVO, selectedProfesion, bas)
						: 0)
						+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO, cat, sexo) : 0)
						+ (dogfa ? CotizadorUtil.getInstance().getGFA(edadcalculoVO, selectedProfesion, gfa)
								: 0)
						+ (doge ? CotizadorUtil.getInstance().getGE(edadcalculoVO, selectedProfesion, ge)
								: 0)
						+ (dobacy ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, bacy) : 0)
						+ (dogfc ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, gfc) : 0)
						+ (dogpc ? CotizadorUtil.getInstance().getCAT(edadConyugeVO, gpc,
								((sexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO))
								: 0)
						+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
						+ (doccomp ? CotizadorUtil.getInstance().getBACY(getEdadVO(ccompEdad), ccomp) : 0)
						+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp1Edad),
								catcomp1, catcomp1sexo.getValue().intValue())
								: 0)
						+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp2Edad),
								catcomp2, catcomp2sexo.getValue().intValue())
								: 0)
						+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp3Edad),
								catcomp3, catcomp3sexo.getValue().intValue())
								: 0);

				bit *= factorBIT(edadcalculoVO, selectedProfesion);

				primaTotal += (dobas ? CotizadorUtil.getInstance().getBAS(edadcalculoVO, selectedProfesion,
						bas) : 0)
						+ (docii ? CotizadorUtil.getInstance().getCII(edadcalculoVO, selectedProfesion, cii)
								: 0)
						+ (docma ? CotizadorUtil.getInstance().getCMA(edadcalculoVO, selectedProfesion, cma)
								: 0)
						+ (dotiba ? CotizadorUtil.getInstance().getTIBA(edadcalculoVO, selectedProfesion,
								tiba) : 0)
						+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO, cat, sexo) : 0)
						+ (dogfa ? CotizadorUtil.getInstance().getGFA(edadcalculoVO, selectedProfesion, gfa)
								: 0)
						+ (doge ? CotizadorUtil.getInstance().getGE(edadcalculoVO, selectedProfesion, ge)
								: 0)
						+ (dobacy ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, bacy) : 0)
						+ (dogfc ? CotizadorUtil.getInstance().getBACY(edadConyugeVO, gfc) : 0)
						+ (dogpc ? CotizadorUtil.getInstance().getCAT(edadConyugeVO, gpc,
								((sexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO))
								: 0)
						+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
						+ (doccomp ? CotizadorUtil.getInstance().getBACY(getEdadVO(ccompEdad), ccomp) : 0)
						+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp1Edad),
								catcomp1, catcomp1sexo.getValue().intValue())
								: 0)
						+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp2Edad),
								catcomp2, catcomp2sexo.getValue().intValue())
								: 0)
						+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp3Edad),
								catcomp3, catcomp3sexo.getValue().intValue())
								: 0) + (dobit ? bit : 0);
				primaTotal += primaExcedente ;
				
				primaTotal /= pago.getValue().intValue();
				primaTotalInput.setText("" + showBaneficio(primaTotal));
			}
			
			if( doNcotizacion ){
				gfaCheck.setEnabled( bas > 98503 ? true : (dogfa = false) );
				gfcCheck.setEnabled( (bas > 98503 ? true : (dogfc = false)  ) && doconyuge);
				gfhSpinner.setEnabled( bas > 98503 ? true : (dogfh = false) );
				gfaCheck.setChecked(dogfa);
				gfcCheck.setChecked(dogfc);
			}
			
			primaExcedenteEdit.setText( primaExcedente + "" );
			
			basSumaEdit.setText("" + (dobas ? showBaneficio(bas) : "Excluida"));
			ciiSumaEdit.setText("" + (docii ? showBaneficio(cii) : "Excluida"));
			cmaSumaEdit.setText("" + (docma ? showBaneficio(cma) : "Excluida"));
			tibaSumaEdit.setText("" + (dotiba ? showBaneficio(tiba) : "Excluida"));
			catSumaEdit.setText("" + (docat ? showBaneficio(cat) : "Excluida"));
			gfaSumaEdit.setText("" + (dogfa ? showBaneficio(gfa) : "Excluida"));
			geSumaEdit.setText("" + (doge ? showBaneficio(ge) : "Excluida"));
			bacySumaEdit.setText("" + (dobacy ? showBaneficio(bacy) : "Excluida"));
			gfcSumaEdit.setText("" + (dogfc ? showBaneficio(gfc) : "Excluida"));
			gpcSumaEdit.setText("" + (dogpc ? showBaneficio(gpc) : "Excluida"));
			gfhSumaEdit.setText(""
					+ ((selectedGFH.getValue().floatValue() > 0) ? showBaneficio(gfh)
							: "Excluida"));
			ccompSumaEdit.setText("" + (doccomp ? showBaneficio(ccomp) : "Excluida"));
			catcomp1SumaEdit.setText("" + (docatcomp1 ? showBaneficio(catcomp1) : "Excluida"));
			catcomp2SumaEdit.setText("" + (docatcomp2 ? showBaneficio(catcomp2) : "Excluida"));
			catcomp3SumaEdit.setText("" + (docatcomp3 ? showBaneficio(catcomp3) : "Excluida"));

			bitSuma.setText("" + (dobit ? "Cubierta" : "Excluida"));

			conyugeCheck.setText("Cónyuge "
					+ ((sexo == CotizadorUtil.MASCULINO) ? "(Mujer)" : "(Hombre)"));

			basPrima.setText(""
					+ (dobas ? showBaneficio(CotizadorUtil.getInstance().getBAS(edadcalculoVO,
							selectedProfesion, bas)) : "Excluida"));
			ciiPrima.setText(""
					+ (docii ? showBaneficio(CotizadorUtil.getInstance().getCII(edadcalculoVO,
							selectedProfesion, cii)) : "Excluida"));
			cmaPrima.setText(""
					+ (docma ? showBaneficio(CotizadorUtil.getInstance().getCMA(edadcalculoVO,
							selectedProfesion, cma)) : "Excluida"));
			tibaPrima.setText(""
					+ (dotiba ? showBaneficio(CotizadorUtil.getInstance().getTIBA(edadcalculoVO,
							selectedProfesion, tiba)) : "Excluida"));
			catPrima.setText(""
					+ (docat ? showBaneficio(CotizadorUtil.getInstance().getCAT(edadVO, cat, sexo)) : "Excluida"));
			gfaPrima.setText(""
					+ (dogfa ? showBaneficio(CotizadorUtil.getInstance().getGFA(edadcalculoVO,
							selectedProfesion, gfa)) : "Excluida"));
			gePrima.setText(""
					+ (doge ? showBaneficio(CotizadorUtil.getInstance().getGE(edadcalculoVO, selectedProfesion,
							ge)) : "Excluida"));
			bacyPrima.setText(""
					+ (dobacy ? showBaneficio(CotizadorUtil.getInstance().getBACY(edadConyugeVO, bacy))
							: "Excluida"));
			gfcPrima.setText(""
					+ (dogfc ? showBaneficio(CotizadorUtil.getInstance().getBACY(edadConyugeVO, gfc))
							: "Excluida"));
			gpcPrima.setText(""
					+ (dogpc ? showBaneficio(CotizadorUtil.getInstance().getCAT(edadConyugeVO, gpc,
							((sexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO)))
							: "Excluida"));
			gfhPrima.setText(""
					+ ((selectedGFH.getValue().floatValue() > 0) ? showBaneficio(CotizadorUtil.getInstance().getGFH(
							selectedGFH, gfh)) : "Excluida"));
			ccompPrima.setText(""
					+ (doccomp ? showBaneficio(CotizadorUtil.getInstance().getBACY(getEdadVO(ccompEdad), ccomp))
							: "Excluida"));
			catcomp1Prima.setText(""
					+ (docatcomp1 ? showBaneficio(CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp1Edad),
							catcomp1, catcomp1sexo.getValue().intValue()))
							: "Excluida"));
			catcomp2Prima.setText(""
					+ (docatcomp2 ? showBaneficio(CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp2Edad),
							catcomp2, catcomp2sexo.getValue().intValue()))
							: "Excluida"));
			catcomp3Prima.setText(""
					+ (docatcomp3 ? showBaneficio(CotizadorUtil.getInstance().getCAT(getEdadVO(catcomp3Edad),
							catcomp3, catcomp3sexo.getValue().intValue()))
							: "Excluida"));
			bitPrima.setText("" + (dobit ? showBaneficio(bit) : "Excluida"));

		}
	}

	private double factorBIT( EdadVO pEdad, ProfesionVO pProfesion){
		if(CotizadorUtil.getInstance().isDoPFT())
		{
			return pEdad.getBit()* pProfesion.getInvalidez() / 100;	
		}
		return pEdad.getBitn()* pProfesion.getInvalidez() / 100;
	}
	
	private void porSumaAsegurada(boolean ban) {
		if (ban) {
			basSumaEdit.setTextColor(Color.parseColor("#000000"));
			bitSuma.setTextColor(Color.parseColor("#000000"));
			ciiSumaEdit.setTextColor(Color.parseColor("#000000"));
			cmaSumaEdit.setTextColor(Color.parseColor("#000000"));
			tibaSumaEdit.setTextColor(Color.parseColor("#000000"));
			catSumaEdit.setTextColor(Color.parseColor("#000000"));
			gfaSumaEdit.setTextColor(Color.parseColor("#000000"));
			geSumaEdit.setTextColor(Color.parseColor("#000000"));
			bacySumaEdit.setTextColor(Color.parseColor("#000000"));
			gfcSumaEdit.setTextColor(Color.parseColor("#000000"));
			gpcSumaEdit.setTextColor(Color.parseColor("#000000"));
			ccompSumaEdit.setTextColor(Color.parseColor("#000000"));
			catcomp1SumaEdit.setTextColor(Color.parseColor("#000000"));
			catcomp2SumaEdit.setTextColor(Color.parseColor("#000000"));
			catcomp3SumaEdit.setTextColor(Color.parseColor("#000000"));

			basPrima.setTextColor(Color.parseColor("#818084"));
			bitPrima.setTextColor(Color.parseColor("#818084"));
			ciiPrima.setTextColor(Color.parseColor("#818084"));
			cmaPrima.setTextColor(Color.parseColor("#818084"));
			tibaPrima.setTextColor(Color.parseColor("#818084"));
			catPrima.setTextColor(Color.parseColor("#818084"));
			gfaPrima.setTextColor(Color.parseColor("#818084"));
			gePrima.setTextColor(Color.parseColor("#818084"));
			bacyPrima.setTextColor(Color.parseColor("#818084"));
			gfcPrima.setTextColor(Color.parseColor("#818084"));
			gpcPrima.setTextColor(Color.parseColor("#818084"));
			ccompPrima.setTextColor(Color.parseColor("#818084"));
			catcomp1Prima.setTextColor(Color.parseColor("#818084"));
			catcomp2Prima.setTextColor(Color.parseColor("#818084"));
			catcomp3Prima.setTextColor(Color.parseColor("#818084"));
		} else {
			basSumaEdit.setTextColor(Color.parseColor("#818084"));
			bitSuma.setTextColor(Color.parseColor("#818084"));
			ciiSumaEdit.setTextColor(Color.parseColor("#818084"));
			cmaSumaEdit.setTextColor(Color.parseColor("#818084"));
			tibaSumaEdit.setTextColor(Color.parseColor("#818084"));
			catSumaEdit.setTextColor(Color.parseColor("#818084"));
			gfaSumaEdit.setTextColor(Color.parseColor("#818084"));
			geSumaEdit.setTextColor(Color.parseColor("#818084"));
			bacySumaEdit.setTextColor(Color.parseColor("#818084"));
			gfcSumaEdit.setTextColor(Color.parseColor("#818084"));
			gpcSumaEdit.setTextColor(Color.parseColor("#818084"));
			ccompSumaEdit.setTextColor(Color.parseColor("#818084"));
			catcomp1SumaEdit.setTextColor(Color.parseColor("#818084"));
			catcomp2SumaEdit.setTextColor(Color.parseColor("#818084"));
			catcomp3SumaEdit.setTextColor(Color.parseColor("#818084"));

			basPrima.setTextColor(Color.parseColor("#000000"));
			bitPrima.setTextColor(Color.parseColor("#000000"));
			ciiPrima.setTextColor(Color.parseColor("#000000"));
			cmaPrima.setTextColor(Color.parseColor("#000000"));
			tibaPrima.setTextColor(Color.parseColor("#000000"));
			catPrima.setTextColor(Color.parseColor("#000000"));
			gfaPrima.setTextColor(Color.parseColor("#000000"));
			gePrima.setTextColor(Color.parseColor("#000000"));
			bacyPrima.setTextColor(Color.parseColor("#000000"));
			gfcPrima.setTextColor(Color.parseColor("#000000"));
			gpcPrima.setTextColor(Color.parseColor("#000000"));
			ccompPrima.setTextColor(Color.parseColor("#000000"));
			catcomp1Prima.setTextColor(Color.parseColor("#000000"));
			catcomp2Prima.setTextColor(Color.parseColor("#000000"));
			catcomp3Prima.setTextColor(Color.parseColor("#000000"));
		}
		primaTotalInput.setEnabled(!ban);
		basSumaEdit.setEnabled(ban);
		ciiSumaEdit.setEnabled(ban);
		cmaSumaEdit.setEnabled(ban);
		tibaSumaEdit.setEnabled(ban);
		catSumaEdit.setEnabled(ban);
		gfaSumaEdit.setEnabled(ban);
		geSumaEdit.setEnabled(ban);
		bacySumaEdit.setEnabled(ban);
		gfcSumaEdit.setEnabled(ban);
		gpcSumaEdit.setEnabled(ban);
		ccompSumaEdit.setEnabled(ban);
		catcomp1SumaEdit.setEnabled(ban);
		catcomp2SumaEdit.setEnabled(ban);
		catcomp3SumaEdit.setEnabled(ban);
	}

	private void bloquearTodo() {
		basCheck.setEnabled(dobas = false);
		inhabilitar(basSumaEdit, dobas, true);
		inhabilitar(basPrima, dobas, false);
		bitCheck.setEnabled(dobit = false);
		ciiCheck.setEnabled(docii = false);
		inhabilitar(ciiSumaEdit, docii, true);
		inhabilitar(ciiPrima, docii, false);
		cmaCheck.setEnabled(docma = false);
		inhabilitar(cmaSumaEdit, docma, true);
		inhabilitar(cmaPrima, docma, false);
		tibaCheck.setEnabled(dotiba = false);
		inhabilitar(tibaSumaEdit, dotiba, true);
		inhabilitar(tibaPrima, dotiba, false);
		catCheck.setEnabled(docat = false);
		inhabilitar(catSumaEdit, docat, true);
		inhabilitar(catPrima, docat, false);
		gfaCheck.setEnabled(dogfa = false);
		inhabilitar(gfaSumaEdit, dogfa, true);
		inhabilitar(gfaPrima, dogfa, false);
		geCheck.setEnabled(doge = false);
		inhabilitar(geSumaEdit, doge, true);
		inhabilitar(gePrima, doge, false);
		conyugeCheck.setEnabled(doconyuge = false);
		ccompCheck.setEnabled(doccomp = false);
		inhabilitar(ccompSumaEdit, doccomp, true);
		inhabilitar(ccompPrima, doccomp, false);
		catcomp1Check.setEnabled(docatcomp1 = false);
		inhabilitar(catcomp1SumaEdit, docatcomp1, true);
		inhabilitar(catcomp1Prima, docatcomp1, false);
		catcomp2Check.setEnabled(docatcomp2 = false);
		inhabilitar(catcomp2SumaEdit, docatcomp2, true);
		inhabilitar(catcomp2Prima, docatcomp2, false);
		catcomp3Check.setEnabled(docatcomp3 = false);
		inhabilitar(catcomp3SumaEdit, docatcomp3, true);
		inhabilitar(catcomp3Prima, docatcomp3, false);
		gfhSpinner.setEnabled( dogfh = false );
		bitSuma.setTextColor( Color.parseColor("#818084"));
		basSumaEdit.setText( "Excluida" );
		ciiSumaEdit.setText( "Excluida" );
		cmaSumaEdit.setText( "Excluida" );
		tibaSumaEdit.setText( "Excluida" );
		catSumaEdit.setText( "Excluida" );
		gfaSumaEdit.setText( "Excluida" );
		geSumaEdit.setText( "Excluida" );
		bacySumaEdit.setText( "Excluida" );
		gfcSumaEdit.setText( "Excluida" );
		gpcSumaEdit.setText( "Excluida" );
		gfhSumaEdit.setText( "Excluida" );
		ccompSumaEdit.setText( "Excluida" );
		catcomp1SumaEdit.setText( "Excluida" );
		catcomp2SumaEdit.setText( "Excluida" );
		catcomp3SumaEdit.setText( "Excluida" );
		bitSuma.setText( "Excluida" );
		conyugeCheck.setText( "Excluida" );
		basPrima.setText( "Excluida" );
		ciiPrima.setText( "Excluida" );
		cmaPrima.setText( "Excluida" );
		tibaPrima.setText( "Excluida" );
		catPrima.setText( "Excluida" );
		gfaPrima.setText( "Excluida" );
		gePrima.setText( "Excluida" );
		bacyPrima.setText( "Excluida" );
		gfcPrima.setText(  "Excluida" );
		gpcPrima.setText( "Excluida" );
		gfhPrima.setText( "Excluida" );
		ccompPrima.setText( "Excluida" );
		catcomp1Prima.setText( "Excluida" );
		catcomp2Prima.setText( "Excluida" );
		catcomp3Prima.setText( "Excluida" );
		bitPrima.setText( "Excluida" );
	}

	private void inhabilitar(EditText editText, boolean ban, boolean enable) {
		editText.setEnabled( enable ? ban : false );
		editText.setTextColor(ban ? Color.parseColor("#000000") : Color
				.parseColor("#818084"));
	}
	
	private String showBaneficio( double beneficio ){
		long benetem = (long)CotizadorUtil.getInstance().redondea(beneficio);
		String punto = CotizadorUtil.getInstance().redondea((beneficio) - benetem) + "";
		String BENEFICIO = benetem + "";
		for(int i = 1; i < punto.length(); i++){
			BENEFICIO += punto.charAt(i);
		}
		return BENEFICIO;
	}

}

class CotizadorInflateTask extends AsyncTask<String, String, String>
{
	
	
	private DelayedInitter initter;
	private View generatedView;
	
	
	public CotizadorInflateTask( DelayedInitter initter){
		this.initter = initter;
		
	}
	protected String doInBackground(String... key) {
		try{
		Thread.sleep(1500);
		generatedView = initter.inProcess();
		}catch(Exception error)
		{
			
		}
	    return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		initter.delayedInit(generatedView);
	}
	
}
