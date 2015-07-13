package com.jaguarlabs.sipac.fragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.jaguarlabs.sipac.MainApplicationActivity;
import com.jaguarlabs.sipac.R;
import com.jaguarlabs.sipac.components.VerticalButton;
import com.jaguarlabs.sipac.db.CommonDAO;
import com.jaguarlabs.sipac.db.SipacOpenHelper;
import com.jaguarlabs.sipac.model.DataModel;
import com.jaguarlabs.sipac.tables.CotizacionTable;
import com.jaguarlabs.sipac.util.CotizadorUtil;
import com.jaguarlabs.sipac.util.DelayedInitter;
import com.jaguarlabs.sipac.util.GEOmmittedError;
import com.jaguarlabs.sipac.util.IKeyboardHider;
import com.jaguarlabs.sipac.util.IRangeCallback;
import com.jaguarlabs.sipac.util.OnFocusRangeChangeListener;
import com.jaguarlabs.sipac.util.OverFlowError;
import com.jaguarlabs.sipac.vo.CotizacionVO;
import com.jaguarlabs.sipac.vo.EdadVO;
import com.jaguarlabs.sipac.vo.ProfesionVO;
import com.jaguarlabs.sipac.vo.ValueVO;


public class CotizacionFragment extends RightFragment implements
		IKeyboardHider, DelayedInitter {
	public static double MAX = 3000000;
	public static double MIN = 30000;
	public static double MINGE = 10000;
	public static final double MIN_GASTOS_FUNERARIOS = 34476;

	public static final int MASCULINO = R.id.radioMale;
	public static final int FEMENINO = R.id.radioFemale;

	public static final int ENABLE_GPC = 8;
	public static final int ENABLE_CAT1 = 4;
	public static final int ENABLE_CAT2 = 2;
	public static final int ENABLE_CAT3 = 1;

	/*--------------------------------------------- Listeners ---------------------------------------------------------*/
	private OnFocusRangeChangeListener basHandler, ciiHandler, cmaHandler,
			tibaHandler, catHandler, gfaHandler, geHandler, bacyHandler,
			gfcHandler, gpcHandler, gfhHandler, ccompHandler, catcomp1Handler,
			catcomp2Handler, catcomp3Handler, conyugeEdadHandler,
			ccompEdadHandler, catcomp1EdadHandler, catcomp2EdadHandler,
			catcomp3EdadHandler, primaTotalHandler, edadHandler,
			primaExcedenteHandle;

	private OverflowHandler discounts;

	/*------------------------------------------ Components ------------------------------------------------------------*/
	private Spinner profesionesSpinner, gfhSpinner, ccompSpinner,
			catcomp1Spinner, catcomp2Spinner, catcomp3Spinner, pagoSpinner,
			calculoSpinner;

	private EditText realAgeInput, conyugueAgeEdit, ccompEdadInput,
			catcomp1EdadInput, catcomp2EdadInput, catcomp3EdadInput, basPrima,
			ciiPrima, cmaPrima, tibaPrima, catPrima, gfaPrima, gePrima,
			bacyPrima, gfcPrima, gpcPrima, gfhPrima, ccompPrima, catcomp1Prima,
			catcomp2Prima, catcomp3Prima, primaTotalInput, ageEdit,
			basSumaEdit, ciiSumaEdit, cmaSumaEdit, tibaSumaEdit, catSumaEdit,
			gfaSumaEdit, geSumaEdit, bacySumaEdit, gfcSumaEdit, gpcSumaEdit,
			gfhSumaEdit, ccompSumaEdit, catcomp1SumaEdit, catcomp2SumaEdit,
			catcomp3SumaEdit, bitSuma, bitPrima, primaExcedenteInput,
			aseguradoInput;

	private TextView extraPrimas, primaTotalT;

	private RadioGroup radioSexGroup, coberturasRadio, cotizacionRadio;
	private CheckBox fumaCheck, ciiCheck, cmaCheck, tibaCheck, catCheck,
			gfaCheck, geCheck, conyugueCheck, bacyCheck, gfcCheck, gpcCheck,
			ccompCheck, catcomp1Check, catcomp2Check, catcomp3Check, bitCheck,
			basCheck, lastChecked;

	/*--------------------------------------- Variables -----------------------------------------------------------------*/

	private boolean fuma, dobas = true, docii, docma, dotiba, docat, dogfa,
			doge, dobacy, doconyuge, dogfc, dogpc, doccomp, docatcomp1,
			docatcomp2, docatcomp3, dobit, dogfh, doNcotizacion, doedadcii,
			doedadcma, doedadbit, doedadtiba, doedadcat, doedadgpc,
			doedadcatcomp1, doedadcatcomp2, doedadcatcomp3;

	private int sexo, edad, edadConyugue, ccompEdad, catcomp1Edad,
			catcomp2Edad, catcomp3Edad, estadosCP, nHijos;
	private double bas, cii, cma, tiba, cat, gfa, ge, bacy, gfc, gpc, gfh,
			ccomp, catcomp1, catcomp2, catcomp3, primaTotal, primaTotalTem,
			bit, primaExcedente;

	private ProfesionVO selectedProfesion;
	private ValueVO<Double> selectedGFH;
	private ValueVO<Integer> ccompsexo, catcomp1sexo, catcomp2sexo,
			catcomp3sexo, selectedPago, selectedCalculo;
	private List<ViewGroup> cotizadorPages;
	private ProgressDialog dialog;

	/*------------------------------------------  Overrides --------------------------------------------------------------*/

	@Override
	protected void makeView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		discounts = new OverflowHandler(1000000, 180000, 344760, 180000,
				1000000, 180000, MAX, 1000000, 1000000, 1000000, MAX, MAX, MAX,
				MAX, MAX);

		mainView = inflater.inflate(
				com.jaguarlabs.sipac.R.layout.fragment_cotizacion, container,
				false);
		mainView.findViewById(R.id.focuStarter).requestFocus();

		ViewGroup flipper = (ViewGroup) mainView
				.findViewById(R.id.mainCotizadorHolder);
		flipper.setVisibility(View.INVISIBLE);
		dialog = ProgressDialog.show(caller.getMContext(), "Progreso",
				"Cargando Cotizador", true);
		(new CotizadorInflateTask(this)).execute();

	}

	@Override
	public void delayedInit(View pView) {
		Button back;
		ViewFlipper container = (ViewFlipper) mainView
				.findViewById(R.id.cotizadorFlipper);
		back = (Button) mainView.findViewById(R.id.back);
		MainApplicationActivity.MainApplicationOnTouchListener interaction = new MainApplicationActivity.MainApplicationOnTouchListener(
				listener);
		back.setOnTouchListener(interaction);
		back.setOnClickListener(interaction);

		for (ViewGroup item : cotizadorPages) {
			container.addView(item);
		}
		initializeComponents();
		setUpProfesiones();
		setUpHijos();
		setUpSexo();
		setupListeners();
		setUpPago();
		setUpCalculo();
		initalizeVariables();
		ViewGroup flipper = (ViewGroup) mainView
				.findViewById(R.id.mainCotizadorHolder);
		flipper.setVisibility(View.VISIBLE);
		dialog.cancel();
	}

	@Override
	public View inProcess() {
		caller.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					cotizadorPages = new ArrayList<ViewGroup>();
					cotizadorPages.add((ViewGroup) caller.getLayoutInflater()
							.inflate(R.layout.page_general_cotizador, null));
					cotizadorPages.add((ViewGroup) caller.getLayoutInflater()
							.inflate(R.layout.page_cobertura_cotizador, null));
					cotizadorPages.add((ViewGroup) caller.getLayoutInflater()
							.inflate(R.layout.page_conyugue_cotizador, null));
					cotizadorPages.add((ViewGroup) caller.getLayoutInflater()
							.inflate(R.layout.page_hijos_cotizador, null));
				} catch (Exception error) {
					Log.e("Error", "Error Generando cotizador");
				}
			}
		});
		return null;
	}

	public void hideKeyboard(EditText currentField) {
		InputMethodManager imm = (InputMethodManager) caller
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (currentField != null)
			imm.hideSoftInputFromWindow(currentField.getWindowToken(), 0);
	}

	/*---------------------------------------- Funciones --------------------------------------------------------------*/
	private void initalizeVariables() {
		selectedCalculo = (ValueVO<Integer>) calculoSpinner
				.getItemAtPosition(0);
		cambioCalculo(true);
	}

	private void cambioCalculo(Boolean doRefresh) {
		estadosCP = 0;
		edad = 15;
		edadConyugue = 15;
		primaExcedente = 0;
		ccompEdad = 15;
		catcomp1Edad = 15;
		catcomp2Edad = 15;
		catcomp3Edad = 15;
		sexo = MASCULINO;
		ccompsexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp1sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp2sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		catcomp3sexo = (ValueVO<Integer>) ccompSpinner.getItemAtPosition(0);
		selectedGFH = (ValueVO<Double>) gfhSpinner.getItemAtPosition(0);
		selectedProfesion = (ProfesionVO) profesionesSpinner
				.getItemAtPosition(0);
		selectedPago = (ValueVO<Integer>) pagoSpinner.getItemAtPosition(0);

		fuma = true;
		CotizadorUtil.getInstance().setDoPFT(true);
		doNcotizacion = true;
		primaTotal = 0;
		primaTotalTem = 0;
		estadosCP = 0;
		dobas = true;
		dobit = false;
		docii = false;
		docma = false;
		dotiba = false;
		docat = false;
		doge = false;
		doconyuge = false;
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
		cotizacionRadio.check(R.id.radioNueva);
		coberturasRadio.check(R.id.radiodevolucion);
		// calculoSpinner.setSelection( 0 );
		pagoSpinner.setSelection(0);
		gfhSpinner.setSelection(0);
		profesionesSpinner.setSelection(0);
		ccompSpinner.setSelection(0);
		profesionesSpinner.setSelection(0);
		catcomp1Spinner.setSelection(0);
		catcomp2Spinner.setSelection(0);
		catcomp3Spinner.setSelection(0);

		if (doRefresh) {

			restoreGFError();
		} else {
			dogfa = false;
			dogfc = false;
			dogfh = false;
		}
	}

	private void restoreGFError() {
		dogfa = false;
		dogfc = false;
		dogfh = false;
		refresh();
	}

	private void initializeComponents() {
		aseguradoInput = (EditText) mainView.findViewById(R.id.aseguradoInput);
		realAgeInput = (EditText) mainView.findViewById(R.id.calculateAge);
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

		profesionesSpinner = (Spinner) mainView
				.findViewById(R.id.profesionesSpinner);
		gfhSpinner = (Spinner) mainView.findViewById(R.id.gfhSpinner);
		ccompSpinner = (Spinner) mainView.findViewById(R.id.ccompSpinner);
		catcomp1Spinner = (Spinner) mainView.findViewById(R.id.catcomp1Spinner);
		catcomp2Spinner = (Spinner) mainView.findViewById(R.id.catcomp2Spinner);
		catcomp3Spinner = (Spinner) mainView.findViewById(R.id.catcomp3Spinner);

		radioSexGroup = (RadioGroup) mainView.findViewById(R.id.radioSex);
		coberturasRadio = (RadioGroup) mainView
				.findViewById(R.id.radioCoberturas);
		cotizacionRadio = (RadioGroup) mainView
				.findViewById(R.id.radioTipoCobertura);

		basCheck = (CheckBox) mainView.findViewById(R.id.basCheck);
		fumaCheck = (CheckBox) mainView.findViewById(R.id.fumaCheck);
		ciiCheck = (CheckBox) mainView.findViewById(R.id.ciiCheck);
		cmaCheck = (CheckBox) mainView.findViewById(R.id.cmaCheck);
		tibaCheck = (CheckBox) mainView.findViewById(R.id.tibaCheck);
		catCheck = (CheckBox) mainView.findViewById(R.id.catCheck);
		gfaCheck = (CheckBox) mainView.findViewById(R.id.gfaCheck);
		geCheck = (CheckBox) mainView.findViewById(R.id.geCheck);

		conyugueCheck = (CheckBox) mainView.findViewById(R.id.conyugueCheck);
		bacyCheck = (CheckBox) mainView.findViewById(R.id.bacyCheck);
		gfcCheck = (CheckBox) mainView.findViewById(R.id.gfcCheck);
		gpcCheck = (CheckBox) mainView.findViewById(R.id.gpcCheck);
		bitCheck = (CheckBox) mainView.findViewById(R.id.bitCheck);

		ccompCheck = (CheckBox) mainView.findViewById(R.id.ccompCheck);
		catcomp1Check = (CheckBox) mainView.findViewById(R.id.catcomp1Check);
		catcomp2Check = (CheckBox) mainView.findViewById(R.id.catcomp2Check);
		catcomp3Check = (CheckBox) mainView.findViewById(R.id.catcomp3Check);

		pagoSpinner = (Spinner) mainView.findViewById(R.id.pagoSpinner);
		calculoSpinner = (Spinner) mainView.findViewById(R.id.calculoSpinner);

		ageEdit = (EditText) mainView.findViewById(R.id.realAge);
		conyugueAgeEdit = (EditText) mainView.findViewById(R.id.conyugueAge);
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

		extraPrimas = (TextView) mainView.findViewById(R.id.extraPrimas);
		primaTotalT = (TextView) mainView.findViewById(R.id.primaHeaderText);
		primaExcedenteInput = (EditText) mainView
				.findViewById(R.id.primaExcedenteInput);
		((VerticalButton) mainView.findViewById(R.id.titularButton))
				.setBackgroundResource(R.drawable.img_tab_act);

	}

	/*-------------------------------------------- Handlers ----------------------------------------------------*/

	private void showCotizacion() {
		CotizacionVO cotizacionVO = new CotizacionVO(aseguradoInput.getText()
				.toString(), selectedProfesion, fuma, dobas, dobit, docii,
				docma, dotiba, docat, dogfa, doge, doconyuge, dobacy, dogfc,
				dogpc, dogfh, CotizadorUtil.getInstance().isDoPFT(), doccomp,
				docatcomp1, docatcomp2, docatcomp3, sexo, ccompsexo,
				catcomp1sexo, catcomp2sexo, catcomp3sexo, getEdadVO(edad),
				getEdadCalculoVO(edad, sexo, fuma), getEdadVO(edadConyugue),
				getEdadVO(ccompEdad), getEdadVO(catcomp1Edad),
				getEdadVO(catcomp2Edad), getEdadVO(catcomp3Edad), nHijos,
				primaExcedente, (selectedCalculo.getValue() == 1 ? primaTotal
						: (primaExcedente + primaTotal)
								/ selectedPago.getValue().intValue()), bas,
				bit, cii, cma, tiba, cat, gfa, ge, bacy, gfc, gpc, gfh, ccomp,
				catcomp1, catcomp2, catcomp3, selectedGFH,
				selectedPago.getLabel());

		DataModel.getInstance().setEstadoCotizador(cotizacionVO);

		Intent intent = new Intent(mainView.getContext(), CotizacionTable.class);

		startActivity(intent);
		caller.overridePendingTransition(R.anim.slide_out_left,
				R.anim.slide_in_left);
	}

	private void setupListeners() {

		((Button) mainView.findViewById(R.id.clearButton))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						calculoSpinner.setSelection(0);
						initalizeVariables();
					}
				});

		((Button) mainView.findViewById(R.id.showCotizacion))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						showCotizacion();
					}
				});
		/*-------------------------------- Text Fields Handlers -----------------------------------------------*/

		primaExcedenteHandle = new OnFocusRangeChangeListener<Double>(0.0, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						primaExcedente = primaExcedenteInput.getText()
								.toString().equals("") ? 0.0 : value;
						refresh();
					}
				}, primaExcedenteInput, this);

		primaTotalHandler = new OnFocusRangeChangeListener<Double>(
				new Double(1), MAX, new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						if (primaTotalInput.getText().toString().equals("")) {
							value = 0;
						}
						primaTotal = value;
						refresh();
					}
				}, primaTotalInput, this);

		basHandler = new OnFocusRangeChangeListener<Double>(MIN, 344760.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						bas = (valid ? value : (value <= MIN) ? MIN : MAX);
						refresh();
					}
				}, basSumaEdit, this);

		geHandler = new OnFocusRangeChangeListener<Double>(MINGE, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ge = (valid ? value : doNcotizacion ? Math.min(
								Math.max(MINGE, value), bas) : Math.min(
								Math.max(MINGE, value), MAX));
						refresh();
					}
				}, geSumaEdit, this);

		ciiHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cii = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), MAX));
						refresh();
					}
				}, ciiSumaEdit, this);

		cmaHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cma = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), MAX));
						refresh();
					}
				}, cmaSumaEdit, this);

		tibaHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						tiba = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), MAX));
						refresh();
					}
				}, tibaSumaEdit, this);

		catHandler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						cat = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), 1000000.0));
						refresh();
					}
				}, catSumaEdit, this);

		gfaHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfa = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), (int) (bas * .35))
								: Math.min(Math.max(MIN, value), 180000.0));
						refresh();
					}
				}, gfaSumaEdit, this);

		bacyHandler = new OnFocusRangeChangeListener<Double>(MIN, 344760.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						bacy = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), MAX));
						refresh();
					}
				}, bacySumaEdit, this);

		gfcHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfc = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), (int) (bas * .35))
								: Math.min(Math.max(MIN, value), 180000.0));
						refresh();
					}
				}, gfcSumaEdit, this);

		catcomp1Handler = new OnFocusRangeChangeListener<Double>(MIN,
				1000000.0, new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp1 = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), 1000000.0));
						refresh();
					}
				}, catcomp1SumaEdit, this);

		catcomp2Handler = new OnFocusRangeChangeListener<Double>(MIN,
				1000000.0, new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp2 = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), 1000000.0));
						refresh();
					}
				}, catcomp2SumaEdit, this);

		catcomp3Handler = new OnFocusRangeChangeListener<Double>(MIN,
				1000000.0, new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp3 = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), 1000000.0));
						refresh();
					}
				}, catcomp3SumaEdit, this);

		gfhHandler = new OnFocusRangeChangeListener<Double>(MIN, 180000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gfh = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), (int) (bas * .35))
								: Math.min(Math.max(MIN, value), 180000.0));
						refresh();
					}
				}, gfhSumaEdit, this);

		gpcHandler = new OnFocusRangeChangeListener<Double>(MIN, 1000000.0,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						gpc = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(
								Math.max(MIN, value), 1000000.0));
						refresh();
					}
				}, gpcSumaEdit, this);

		ccompHandler = new OnFocusRangeChangeListener<Double>(MIN, MAX,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ccomp = valid ? value : (doNcotizacion ? Math.min(
								Math.max(MIN, value), bas) : Math.min(Math.max(
								MIN, value), (ccompEdad < 18 ? 3000000
								: 100000000)));
						refresh();
					}
				}, ccompSumaEdit, this);

		conyugeEdadHandler = new OnFocusRangeChangeListener<Integer>(15, 70,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						edadConyugue = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, conyugueAgeEdit, this);

		edadHandler = new OnFocusRangeChangeListener<Integer>(15, 70,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						edad = (int) (valid ? value : Math.max(15, value));
						refresh();
					}
				}, ageEdit, this);

		ccompEdadHandler = new OnFocusRangeChangeListener<Integer>(15, 70,
				new IRangeCallback() {
					@Override
					public void doAfterValidation(double value, boolean valid) {
						ccompEdad = (int) (valid ? value : (value <= 15) ? 15
								: value);
						refresh();
					}
				}, ccompEdadInput, this);

		catcomp1EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp1Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp1EdadInput, this);

		catcomp2EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp2Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp2EdadInput, this);

		catcomp3EdadHandler = new OnFocusRangeChangeListener<Integer>(15, 65,
				new IRangeCallback() {

					@Override
					public void doAfterValidation(double value, boolean valid) {
						catcomp3Edad = (int) (valid ? value
								: (value <= 15) ? 15 : value);
						refresh();
					}
				}, catcomp3EdadInput, this);

		primaExcedenteInput.setOnFocusChangeListener(primaExcedenteHandle);
		ageEdit.setOnFocusChangeListener(edadHandler);
		gfcSumaEdit.setOnFocusChangeListener(gfcHandler);
		bacySumaEdit.setOnFocusChangeListener(bacyHandler);
		conyugueAgeEdit.setOnFocusChangeListener(conyugeEdadHandler);
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

		/*-----------------------------------  Check Listeners ----------------------------------------*/

		fumaCheck.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				fuma = ((CheckBox) v).isChecked();
				refresh();
			}
		});

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
				gfa = dogfa ? (doNcotizacion ? (int) (bas * .35) : 10000) : 0;
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

		conyugueCheck.setOnClickListener(new View.OnClickListener() {
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
				gfc = dogfc ? (int) (doNcotizacion ? (int) (bas * .35) : 10000)
						: 0;
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

		/*-----------------------------------  Spinner  Handlers ---------------------------------------------*/
		profesionesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						selectedProfesion = (ProfesionVO) parent
								.getItemAtPosition(pos);
						refresh();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		gfhSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectedGFH = (ValueVO<Double>) parent.getItemAtPosition(pos);
				nHijos = pos;
				dogfh = (selectedGFH.getValue().floatValue() > 0) ? true
						: false;
				if (selectedGFH.getValue().floatValue() > 0) {
					gfh = doNcotizacion ? (int) (bas * .35) : 10000;
				}
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});

		ccompSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				ccompsexo = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
			}
		});

		calculoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				selectedCalculo = (ValueVO<Integer>) parent
						.getItemAtPosition(pos);
				cambioCalculo(false);
				if (selectedCalculo.getValue() == 1) {
					selectedPago = (ValueVO<Integer>) pagoSpinner
							.getItemAtPosition(0);
					pagoSpinner.setSelection(0);
					bas = doNcotizacion ? 100000 : 10000;
					primaTotal = 0;

				} else {
					selectedPago = (ValueVO<Integer>) pagoSpinner
							.getItemAtPosition(2);
					pagoSpinner.setSelection(2);
					primaTotal = 500;

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
				selectedPago = (ValueVO<Integer>) parent.getItemAtPosition(pos);
				refresh();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		/*---------------------------------------  Radio Group Handlers ---------------------------------------------*/

		coberturasRadio
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.radiodevolucion:
							CotizadorUtil.getInstance().setDoPFT(true);
							break;
						case R.id.radioSinDevolucion:
							CotizadorUtil.getInstance().setDoPFT(false);
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
						case R.id.radioNueva:
							doNcotizacion = true;
							dobas = true;
							break;
						case R.id.radioInclusiones:
							doNcotizacion = false;
							break;
						default:
							break;
						}
						refresh();
					}
				});

		radioSexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				sexo = checkedId;
				refresh();
			}
		});

	}

	/*----------------------------------  UI Refresh --------------------------------------------------------------*/

	private void refreshCotizador() {
		dogfa = false;
		dogfc = false;
		dogfh = false;
		refresh();
	}

	// TODO:

	private void refresh() {
		double sumaAseguradaProvicional;
		realAgeInput.setText("" + getEdadCalculo(edad, sexo, fuma));
		MAX = (edad < 18) ? 3000000 : 100000000;

		basHandler.setMaxRange(MAX);
		primaTotalHandler.setMaxRange(MAX);

		if (doNcotizacion && selectedCalculo.getValue() == 1) {

			bas = dobas ? Math.max(MIN, Math.min(bas, MAX)) : 0;
			gfh = dogfh ? Math.max(MIN_GASTOS_FUNERARIOS,
					Math.min(gfh, (int) (bas * .35))) : 0;
			gfc = dogfc ? Math.max(MIN_GASTOS_FUNERARIOS,
					Math.min(gfc, (int) (bas * .35))) : 0;
			gfa = dogfa ? Math.max(MIN_GASTOS_FUNERARIOS,
					Math.min(gfa, (int) (bas * .35))) : 0;
			catcomp1 = docatcomp1 ? Math.max(MIN, catcomp1) : 0;
			catcomp2 = docatcomp2 ? Math.max(MIN, catcomp2) : 0;
			catcomp3 = docatcomp3 ? Math.max(MIN, catcomp3) : 0;

			basHandler.setMinRange(MIN);

			geHandler.setMaxRange(bas);
			ciiHandler.setMaxRange(bas);
			cmaHandler.setMaxRange(bas);
			tibaHandler.setMaxRange(bas);
			catHandler.setMaxRange(bas);

			gfaHandler.setMaxRange(Math.min(180000.0, bas));
			gfcHandler.setMaxRange(Math.min(180000.0, bas));
			gfhHandler.setMaxRange(Math.min(180000.0, bas));

			gfaHandler.setMinRange(MIN_GASTOS_FUNERARIOS);
			gfcHandler.setMinRange(MIN_GASTOS_FUNERARIOS);
			gfhHandler.setMinRange(MIN_GASTOS_FUNERARIOS);

			bacyHandler.setMaxRange(Math.min(bas, 344760.0));
			catcomp1Handler.setMaxRange(bas);
			catcomp2Handler.setMaxRange(bas);
			catcomp3Handler.setMaxRange(bas);

			gfh = dogfh ? Math.max(34476, Math.min(gfh, (int) (bas * .35))) : 0;
			gfc = dogfc ? Math.max(34476, Math.min(gfc, (int) (bas * .35))) : 0;
			gfa = dogfa ? Math.max(34476, Math.min(gfa, (int) (bas * .35))) : 0;

			cat = Math.max(30000, cat);
			catcomp1 = Math.max(30000, catcomp1);
			catcomp2 = Math.max(30000, catcomp2);
			catcomp3 = Math.max(30000, catcomp3);

			catcomp1Handler.setMinRange(MIN);
			catcomp2Handler.setMinRange(MIN);
			catcomp3Handler.setMinRange(MIN);

			gpcHandler.setMaxRange(bas);
			ccompHandler.setMaxRange(ccompEdad < 18 ? Math.min(3000000, bas)
					: bas);

		} else {
			ciiHandler.setMaxRange(MAX);
			cmaHandler.setMaxRange(MAX);
			tibaHandler.setMaxRange(MAX);
			catHandler.setMaxRange(1000000.0);
			gfaHandler.setMaxRange(180000.0);
			geHandler.setMaxRange(MAX);
			ccompHandler.setMaxRange(ccompEdad < 18 ? 3000000 : 100000000);
			gfcHandler.setMaxRange(180000);
			bacyHandler.setMaxRange(344760.0);
			catcomp1Handler.setMaxRange(1000000.0);
			catcomp2Handler.setMaxRange(1000000.0);
			catcomp3Handler.setMaxRange(1000000.0);
			gfhHandler.setMaxRange(180000.0);
			gpcHandler.setMaxRange(1000000.0);

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

		if (bas < 98503 && doNcotizacion) {
			gfaCheck.setChecked(dogfa = false);
			gfcCheck.setChecked(dogfc = false);
			gfhSpinner.setSelection(0);
			selectedGFH = (ValueVO<Double>) gfhSpinner.getItemAtPosition(0);
			dogfh = false;
		}

		if ((bacy + gfc) > bas && dobacy && dogfc && doNcotizacion) {
			gfc = doNcotizacion ? (int) (bas * .35) : gfc;
			gfc = dogfc ? Math.min(180000, gfc) : 0;
			bacy = dobacy ? Math.min(344760, (bas - gfc)) : 0;
		}

		if (edad < 18)
			if ((bas + gfa + ge) > MAX) {
				if (dogfa) {
					gfa = dogfa ? Math.min(180000, gfa) : 0;
				}

				if (doge) {
					ge = doge ? ((MAX - gfa) / Math.max(1, (doge ? 1 : 0)
							+ (dobas ? 1 : 0))) : 0;
				}

				if (dobas) {
					bas = dobas ? ((MAX - gfa) / Math.max(1, (doge ? 1 : 0)
							+ (dobas ? 1 : 0))) : 0;
				}
			}

		bacy = dobacy ? Math.min(
				doNcotizacion ? Math.min(bas, 344760) : 344760, bacy) : 0;
		tiba = dotiba ? (Math.min(6000000, tiba)) : 0;
		cma = docma ? (Math.min(6000000, cma)) : 0;
		cii = docii ? (Math.min(6000000, cii)) : 0;
		cat = docat ? (Math.min(1000000, cat)) : 0;
		gpc = dogpc ? (Math.min(1000000, gpc)) : 0;
		catcomp1 = docatcomp1 ? (Math.min(1000000, catcomp1)) : 0;
		catcomp2 = docatcomp2 ? (Math.min(1000000, catcomp2)) : 0;
		catcomp3 = docatcomp3 ? (Math.min(1000000, catcomp3)) : 0;

		// TODO:Agregue esto *Julio*

		if (doNcotizacion) {
			tiba = dotiba ? Math.max(30000, Math.min(bas, tiba)) : 0;
			cma = docma ? Math.max(30000, Math.min(bas, cma)) : 0;
			cii = docii ? Math.max(30000, Math.min(bas, cii)) : 0;
			cat = docat ? Math.max(30000, Math.min(bas, cat)) : 0;
			ge = doge ? Math.min(bas, ge) : 0;
			gpc = dogpc ? Math
					.min(1000000, Math.max(30000, Math.min(bas, gpc))) : 0;
			ccomp = doccomp ? Math.max(30000, Math.min(bas, ccomp)) : 0;
			bacy = dobacy ? Math.max(30000, Math.min(bas, bacy)) : 0;
		}

		bacy = Math.min(344760, bacy);

		tiba = Math.min((edad < 18 ? 3000000 : 6000000), tiba);
		cma = Math.min((edad < 18 ? 3000000 : 6000000), cma);
		cii = Math.min((edad < 18 ? 3000000 : 6000000), cii);

		cat = Math.min(1000000, cat);
		gpc = Math.min(1000000, gpc);

		ccomp = Math.min(ccompEdad < 18 ? 3000000 : 100000000, ccomp);
		catcomp1 = Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp1)
				: catcomp1);
		catcomp2 = Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp2)
				: catcomp2);
		catcomp3 = Math.min(1000000, doNcotizacion ? Math.min(bas, catcomp3)
				: catcomp3);

		gfh = Math.min(180000, doNcotizacion ? Math.min(bas, gfh) : gfh);
		gfc = Math.min(180000, doNcotizacion ? Math.min(bas, gfc) : gfc);
		gfa = Math.min(180000, doNcotizacion ? Math.min(bas, gfa) : gfa);

		EdadVO edadcalculoVO = getEdadCalculoVO(edad, sexo, fuma);
		EdadVO edadVO = getEdadVO(edad);
		EdadVO edadConyugeVO = getEdadVO(edadConyugue);

		if (!dobas && !dogfa && !docat && !docatcomp1 && !docatcomp2
				&& !docatcomp3 && !dogpc) {
			dobit = false;
			bitCheck.setChecked(dobit);
			bitCheck.setEnabled(dobit);
		} else {
			bitCheck.setEnabled(true);
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

		if (selectedProfesion.getMillar() == 0
				&& selectedProfesion.getAccidente() == 1
				&& selectedProfesion.getInvalidez() == 1) {
			extraPrimas.setText("No Hay ExtraPrimas");
		} else {
			extraPrimas.setText("Vida: "
					+ (selectedProfesion.getMillar() == -1 ? "Rechazado."
							: +selectedProfesion.getMillar() + " millar.")
					+ "\nAccidentes: "
					+ (selectedProfesion.getAccidente() == -1 ? "Rechazado."
							: +selectedProfesion.getAccidente() + " tantos.")
					+ "\nInvalidez: "
					+ (selectedProfesion.getInvalidez() == -1 ? "Rechazado."
							: +selectedProfesion.getInvalidez() + " tantos."));
		}

		conyugueAgeEdit.setEnabled(doconyuge);

		ageEdit.setText("" + edad);
		conyugueAgeEdit.setText("" + edadConyugue);
		ageEdit.setText("" + edad);
		ccompEdadInput.setText("" + ccompEdad);
		catcomp1EdadInput.setText("" + catcomp1Edad);
		catcomp2EdadInput.setText("" + catcomp2Edad);
		catcomp3EdadInput.setText("" + catcomp3Edad);

		if (selectedProfesion.getMillar() == -1) {
			disableUI();
			return;
		}
		if (selectedProfesion.getAccidente() == -1) {
			doedadcma = docma = false;
			doedadtiba = dotiba = false;
		}
		if (selectedProfesion.getInvalidez() == -1) {
			doedadbit = dobit = false;
			doedadcii = docii = false;
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
		conyugueCheck.setEnabled(true);

		ciiSumaEdit.setEnabled(doedadcii);
		cmaSumaEdit.setEnabled(doedadcma);
		tibaSumaEdit.setEnabled(doedadtiba);
		catSumaEdit.setEnabled(doedadcat);
		gpcSumaEdit.setEnabled(doedadgpc);

		gfhSumaEdit.setEnabled(dogfh);

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

		radioSexGroup.check(sexo);

		fumaCheck.setChecked(fuma);
		ciiCheck.setChecked(docii);
		cmaCheck.setChecked(docma);
		tibaCheck.setChecked(dotiba);
		catCheck.setChecked(docat);
		gfaCheck.setChecked(dogfa);
		geCheck.setChecked(doge);
		gfcCheck.setChecked(dogfc);
		gpcCheck.setChecked(dogpc);
		conyugueCheck.setChecked(doconyuge);
		ccompCheck.setChecked(doccomp);
		catcomp1Check.setChecked(docatcomp1);
		catcomp2Check.setChecked(docatcomp2);
		catcomp3Check.setChecked(docatcomp3);
		bacyCheck.setChecked(dobacy & doconyuge);
		bitCheck.setChecked(dobit);

		if (doNcotizacion) {
			gfaCheck.setEnabled(bas > 98503 ? true : false || !doNcotizacion);
			gfcCheck.setEnabled(((bas > 98503 ? true : false) || !doNcotizacion)
					&& doconyuge);
			gfhSpinner.setEnabled((bas > 98503 ? true : false)
					|| !doNcotizacion);
		}

		porSumaAsegurada(selectedCalculo.getValue() == 1);

		// TODO: Hecho por Julio
		if (doNcotizacion) {
			gfaCheck.setEnabled(bas > 98503 ? true : (dogfa = false));
			gfcCheck.setEnabled((bas > 98503 ? true : (dogfc = false))
					&& doconyuge);
			gfhSpinner.setEnabled(bas > 98503 ? true : (dogfh = false));
			gfaCheck.setChecked(dogfa);
			gfcCheck.setChecked(dogfc);
		}
		// ///////////////////////////

		if (selectedCalculo.getValue() == 0) {
			primaTotalTem = Math.max(0, (primaTotal * selectedPago.getValue()
					.doubleValue()) - primaExcedente);

			primaTotalInput.setEnabled(true);

			discounts = edad < 18 ? new OverflowHandler(1000000, 180000,
					344760, 180000, 1000000, 180000, MAX, 1000000, 1000000,
					1000000, MAX, MAX, MAX, MAX, MAX) : new OverflowHandler(
					1000000, 180000, 344760, 180000, 1000000, 180000, 3000000,
					1000000, 1000000, 1000000, 6000000, 6000000, 6000000,
					6000000, 6000000);

			discounts.resetAll();
			try {
				sumaAseguradaProvicional = getSumaAsegurada(edadcalculoVO,
						selectedProfesion, edadConyugeVO, primaTotalTem, sexo,
						edadVO, getEdadVO(ccompEdad), getEdadVO(catcomp1Edad),
						getEdadVO(catcomp2Edad), getEdadVO(catcomp3Edad),
						catcomp1sexo.getValue().intValue(), catcomp2sexo
								.getValue().intValue(), catcomp3sexo.getValue()
								.intValue());
			} catch (GEOmmittedError error) {
				sumaAseguradaProvicional = 0;
				Toast.makeText(caller, "Error al calcular.", Toast.LENGTH_LONG)
						.show();
			} catch (OverFlowError error) {
				sumaAseguradaProvicional = 0;
				Toast.makeText(caller, "Calculos fuera de limites.",
						Toast.LENGTH_LONG).show();
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CAT)) {
				cat = discounts.getLimiteCAT();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CAT)) {
				cat = discounts.getLowerLimit();
			} else {
				cat = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CII)) {
				cii = discounts.getLimiteCII();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CII)) {
				cii = discounts.getLowerLimit();
			} else {
				cii = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CMA)) {
				cma = discounts.getLimiteCMA();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CMA)) {
				cma = discounts.getLowerLimit();
			} else {
				cma = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_TIBA)) {
				tiba = discounts.getLimiteTIBA();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_TIBA)) {
				tiba = discounts.getLowerLimit();
			} else {
				tiba = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_GFA)) {
				gfa = discounts.getLimiteGFA();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_GFA)) {
				gfa = discounts.getLowerLimit();
			} else {
				gfa = (double) (sumaAseguradaProvicional * (doNcotizacion ? .35
						: 1));
			}

			ge = sumaAseguradaProvicional;

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_BACY)) {
				bacy = discounts.getLimiteBACY();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_BACY)) {
				bacy = discounts.getLowerLimit();
			} else {
				bacy = (double) (sumaAseguradaProvicional * (dogfc ? .65 : 1));
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_GFC)) {
				gfc = discounts.getLimiteGFC();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_GFC)) {
				gfc = discounts.getLowerLimit();
			} else {
				gfc = (double) (sumaAseguradaProvicional * .35);
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_GFH)) {
				gfh = discounts.getLimiteGFH();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_GFH)) {
				gfh = discounts.getLowerLimit();
			} else {
				gfh = (double) (sumaAseguradaProvicional * .35);
			}

			if (discounts.isActivoLowerOverflow(OverflowHandler.OVERFLOW_BAS)) {
				bas = discounts.getLowerLimit();
			} else {
				bas = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CATC)) {
				gpc = discounts.getLimiteCATC();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CATC)) {
				gpc = discounts.getLowerLimit();
			} else {
				gpc = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CCOM)) {
				ccomp = discounts.getLimiteCCOM();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CCOM)) {
				ccomp = discounts.getLowerLimit();
			} else {
				ccomp = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CATCOM1)) {
				catcomp1 = discounts.getLimiteCATCOM1();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CATCOM1)) {
				catcomp1 = discounts.getLowerLimit();
			} else {
				catcomp1 = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CATCOM2)) {
				catcomp2 = discounts.getLimiteCATCOM2();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CATCOM2)) {
				catcomp2 = discounts.getLowerLimit();
			} else {
				catcomp2 = sumaAseguradaProvicional;
			}

			if (discounts.isActivoOverflow(OverflowHandler.OVERFLOW_CATCOM3)) {
				catcomp3 = discounts.getLimiteCATCOM3();
			} else if (discounts
					.isActivoLowerOverflow(OverflowHandler.OVERFLOW_CATCOM3)) {
				catcomp3 = discounts.getLowerLimit();
			} else {
				catcomp3 = sumaAseguradaProvicional;
			}

			if ((cii <= 0 && docii) || (cat <= 0 && docat)
					|| (cma <= 0 && docma) || (tiba <= 0 && dotiba)
					|| (gfa <= 0 && dogfa) || (bacy <= 0 && dobacy)
					|| (ge <= 0 && doge) || (gfc <= 0 && dogfc)
					|| (gfh <= 0 && dogfh) || (bas <= 0 && dobas)
					|| (gpc <= 0 && dogpc) || (ccomp <= 0 && doccomp)
					|| (catcomp1 <= 0 && docatcomp1)
					|| (catcomp2 <= 0 && docatcomp2)
					|| (catcomp3 <= 0 && docatcomp3)) {
				caller.getMessageBuilder()
						.setMessage(
								"No  se puede calcular una cotización. Por favor deseleccione algunos beneficios o aumente el valor de la prima anual");
				caller.getMessageBuilder().create().show();
				return;
			}
			bit = 0;
			if (dobit) {
				bit += (dobas ? CotizadorUtil.getInstance().getBAS(
						edadcalculoVO, selectedProfesion, bas) : 0)
						+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO,
								cat, sexo) : 0)
						+ (dogfa ? CotizadorUtil.getInstance().getGFA(
								edadcalculoVO, selectedProfesion, gfa) : 0)
						+ (doge ? CotizadorUtil.getInstance().getGE(
								edadcalculoVO, selectedProfesion, ge) : 0)
						+ (dobacy ? CotizadorUtil.getInstance().getBACY(
								edadConyugeVO, bacy) : 0)
						+ (dogfc ? CotizadorUtil.getInstance().getBACY(
								edadConyugeVO, gfc) : 0)
						+ (dogpc ? CotizadorUtil.getInstance().getCAT(
								edadConyugeVO, gpc,
								((sexo == FEMENINO) ? MASCULINO : FEMENINO))
								: 0)
						+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
						+ (doccomp ? CotizadorUtil.getInstance().getBACY(
								getEdadVO(ccompEdad), ccomp) : 0)
						+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(
								getEdadVO(catcomp1Edad), catcomp1,
								catcomp1sexo.getValue().intValue()) : 0)
						+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(
								getEdadVO(catcomp2Edad), catcomp2,
								catcomp2sexo.getValue().intValue()) : 0)
						+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(
								getEdadVO(catcomp3Edad), catcomp3,
								catcomp3sexo.getValue().intValue()) : 0);

				bit *= factorBIT(edadcalculoVO, selectedProfesion);
			}
			if (((dogfa && gfa < 34476) || (dogfc && gfc < 34476) || (dogfh && gfh < 34476))
					&& doNcotizacion) {
				Toast.makeText(
						caller,
						"Error al calcular. Por favor verifique los beneficios funerarios",
						Toast.LENGTH_LONG).show();
				refreshCotizador();
				return;
			}

		} else {
			primaTotal = 0;
			bit = 0;
			primaTotalInput.setEnabled(false);
			bit += (dobas ? CotizadorUtil.getInstance().getBAS(edadcalculoVO,
					selectedProfesion, bas) : 0)
					+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO, cat,
							sexo) : 0)
					+ (dogfa ? CotizadorUtil.getInstance().getGFA(
							edadcalculoVO, selectedProfesion, gfa) : 0)
					+ (doge ? CotizadorUtil.getInstance().getGE(edadcalculoVO,
							selectedProfesion, ge) : 0)
					+ (dobacy ? CotizadorUtil.getInstance().getBACY(
							edadConyugeVO, bacy) : 0)
					+ (dogfc ? CotizadorUtil.getInstance().getBACY(
							edadConyugeVO, gfc) : 0)
					+ (dogpc ? CotizadorUtil.getInstance().getCAT(
							edadConyugeVO, gpc,
							((sexo == FEMENINO) ? MASCULINO : FEMENINO)) : 0)
					+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
					+ (doccomp ? CotizadorUtil.getInstance().getBACY(
							getEdadVO(ccompEdad), ccomp) : 0)
					+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp1Edad), catcomp1,
							catcomp1sexo.getValue().intValue()) : 0)
					+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp2Edad), catcomp2,
							catcomp2sexo.getValue().intValue()) : 0)
					+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp3Edad), catcomp3,
							catcomp3sexo.getValue().intValue()) : 0);

			bit *= factorBIT(edadcalculoVO, selectedProfesion);

			primaTotal += (dobas ? CotizadorUtil.getInstance().getBAS(
					edadcalculoVO, selectedProfesion, bas) : 0)
					+ (docii ? CotizadorUtil.getInstance().getCII(
							edadcalculoVO, selectedProfesion, cii) : 0)
					+ (docma ? CotizadorUtil.getInstance().getCMA(
							edadcalculoVO, selectedProfesion, cma) : 0)
					+ (dotiba ? CotizadorUtil.getInstance().getTIBA(
							edadcalculoVO, selectedProfesion, tiba) : 0)
					+ (docat ? CotizadorUtil.getInstance().getCAT(edadVO, cat,
							sexo) : 0)
					+ (dogfa ? CotizadorUtil.getInstance().getGFA(
							edadcalculoVO, selectedProfesion, gfa) : 0)
					+ (doge ? CotizadorUtil.getInstance().getGE(edadcalculoVO,
							selectedProfesion, ge) : 0)
					+ (dobacy ? CotizadorUtil.getInstance().getBACY(
							edadConyugeVO, bacy) : 0)
					+ (dogfc ? CotizadorUtil.getInstance().getBACY(
							edadConyugeVO, gfc) : 0)
					+ (dogpc ? CotizadorUtil.getInstance().getCAT(
							edadConyugeVO, gpc,
							((sexo == FEMENINO) ? MASCULINO : FEMENINO)) : 0)
					+ CotizadorUtil.getInstance().getGFH(selectedGFH, gfh)
					+ (doccomp ? CotizadorUtil.getInstance().getBACY(
							getEdadVO(ccompEdad), ccomp) : 0)
					+ (docatcomp1 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp1Edad), catcomp1,
							catcomp1sexo.getValue().intValue()) : 0)
					+ (docatcomp2 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp2Edad), catcomp2,
							catcomp2sexo.getValue().intValue()) : 0)
					+ (docatcomp3 ? CotizadorUtil.getInstance().getCAT(
							getEdadVO(catcomp3Edad), catcomp3,
							catcomp3sexo.getValue().intValue()) : 0)
					+ (dobit ? bit : 0);
			primaTotal += primaExcedente;
			primaTotal /= selectedPago.getValue().intValue();

		}

		primaExcedenteInput.setText(primaExcedente + "");

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
		gfhSumaEdit
				.setText(""
						+ ((selectedGFH.getValue().floatValue() > 0) ? showBaneficio(gfh)
								: "Excluida"));
		ccompSumaEdit.setText(""
				+ (doccomp ? showBaneficio(ccomp) : "Excluida"));
		catcomp1SumaEdit.setText(""
				+ (docatcomp1 ? showBaneficio(catcomp1) : "Excluida"));
		catcomp2SumaEdit.setText(""
				+ (docatcomp2 ? showBaneficio(catcomp2) : "Excluida"));
		catcomp3SumaEdit.setText(""
				+ (docatcomp3 ? showBaneficio(catcomp3) : "Excluida"));
		bitSuma.setText("" + (dobit ? "Incluida" : "Excluida"));
		conyugueCheck.setText("Conyuge "
				+ ((sexo == MASCULINO) ? "(Mujer)" : "(Hombre)"));

		basPrima.setText(""
				+ (dobas ? formatValue(CotizadorUtil.getInstance().getBAS(
						edadcalculoVO, selectedProfesion, bas)) : "Excluida"));
		ciiPrima.setText(""
				+ (docii ? formatValue(CotizadorUtil.getInstance().getCII(
						edadcalculoVO, selectedProfesion, cii)) : "Excluida"));
		cmaPrima.setText(""
				+ (docma ? formatValue(CotizadorUtil.getInstance().getCMA(
						edadcalculoVO, selectedProfesion, cma)) : "Excluida"));
		tibaPrima.setText(""
				+ (dotiba ? formatValue(CotizadorUtil.getInstance().getTIBA(
						edadcalculoVO, selectedProfesion, tiba)) : "Excluida"));
		catPrima.setText(""
				+ (docat ? formatValue(CotizadorUtil.getInstance().getCAT(
						edadVO, cat, sexo)) : "Excluida"));
		gfaPrima.setText(""
				+ (dogfa ? formatValue(CotizadorUtil.getInstance().getGFA(
						edadcalculoVO, selectedProfesion, gfa)) : "Excluida"));
		gePrima.setText(""
				+ (doge ? formatValue(CotizadorUtil.getInstance().getGE(
						edadcalculoVO, selectedProfesion, ge)) : "Excluida"));
		bacyPrima.setText(""
				+ (dobacy ? formatValue(CotizadorUtil.getInstance().getBACY(
						edadConyugeVO, bacy)) : "Excluida"));
		gfcPrima.setText(""
				+ (dogfc ? formatValue(CotizadorUtil.getInstance().getBACY(
						edadConyugeVO, gfc)) : "Excluida"));
		gpcPrima.setText(""
				+ (dogpc ? formatValue(CotizadorUtil.getInstance().getCAT(
						edadConyugeVO, gpc,
						((sexo == FEMENINO) ? MASCULINO : FEMENINO)))
						: "Excluida"));
		gfhPrima.setText(""
				+ ((selectedGFH.getValue().floatValue() > 0) ? formatValue(CotizadorUtil
						.getInstance().getGFH(selectedGFH, gfh)) : "Excluida"));
		ccompPrima.setText(""
				+ (doccomp ? formatValue(CotizadorUtil.getInstance().getBACY(
						getEdadVO(ccompEdad), ccomp)) : "Excluida"));
		catcomp1Prima.setText(""
				+ (docatcomp1 ? formatValue(CotizadorUtil.getInstance().getCAT(
						getEdadVO(catcomp1Edad), catcomp1,
						catcomp1sexo.getValue().intValue())) : "Excluida"));
		catcomp2Prima.setText(""
				+ (docatcomp2 ? formatValue(CotizadorUtil.getInstance().getCAT(
						getEdadVO(catcomp2Edad), catcomp2,
						catcomp2sexo.getValue().intValue())) : "Excluida"));
		catcomp3Prima.setText(""
				+ (docatcomp3 ? formatValue(CotizadorUtil.getInstance().getCAT(
						getEdadVO(catcomp3Edad), catcomp3,
						catcomp3sexo.getValue().intValue())) : "Excluida"));
		bitPrima.setText("" + (dobit ? formatValue(bit) : "Excluida"));

		primaTotalInput.setText("" + formatValue(primaTotal));
		primaTotalT.setText("Prima Total " + selectedPago.getLabel() + ":");

	}

	private String formatValue(double valor) {
		BigDecimal decimal = new BigDecimal(valor);
		return "" + decimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	/*----------------------------------------- Funciones de ayuda ------------------------------------*/

	private String showBaneficio(double beneficio) {
		long benetem = (long) CotizadorUtil.getInstance().redondea(beneficio);
		String punto = CotizadorUtil.getInstance().redondea(
				(beneficio) - benetem)
				+ "";
		String BENEFICIO = benetem + "";
		for (int i = 1; i < punto.length(); i++) {
			BENEFICIO += punto.charAt(i);
		}
		return BENEFICIO;
	}

	private int prenderBandera(int bandera, int constante) {
		return (bandera | constante);
	}

	private int apagarBandera(int bandera, int constante) {
		return (~constante & bandera);
	}

	private void disableUI() {
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
		conyugueCheck.setEnabled(doconyuge = false);
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
		gfhSpinner.setEnabled(dogfh = false);
		bitSuma.setTextColor(Color.parseColor("#818084"));
		basSumaEdit.setText("Excluida");
		ciiSumaEdit.setText("Excluida");
		cmaSumaEdit.setText("Excluida");
		tibaSumaEdit.setText("Excluida");
		catSumaEdit.setText("Excluida");
		gfaSumaEdit.setText("Excluida");
		geSumaEdit.setText("Excluida");
		bacySumaEdit.setText("Excluida");
		gfcSumaEdit.setText("Excluida");
		gpcSumaEdit.setText("Excluida");
		gfhSumaEdit.setText("Excluida");
		ccompSumaEdit.setText("Excluida");
		catcomp1SumaEdit.setText("Excluida");
		catcomp2SumaEdit.setText("Excluida");
		catcomp3SumaEdit.setText("Excluida");
		bitSuma.setText("Excluida");
		conyugueCheck.setText("Excluida");
		basPrima.setText("Excluida");
		ciiPrima.setText("Excluida");
		cmaPrima.setText("Excluida");
		tibaPrima.setText("Excluida");
		catPrima.setText("Excluida");
		gfaPrima.setText("Excluida");
		gePrima.setText("Excluida");
		bacyPrima.setText("Excluida");
		gfcPrima.setText("Excluida");
		gpcPrima.setText("Excluida");
		gfhPrima.setText("Excluida");
		ccompPrima.setText("Excluida");
		catcomp1Prima.setText("Excluida");
		catcomp2Prima.setText("Excluida");
		catcomp3Prima.setText("Excluida");
		bitPrima.setText("Excluida");
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

	private void inhabilitar(EditText editText, boolean ban, boolean enable) {
		editText.setEnabled(enable ? ban : false);
		editText.setTextColor(ban ? Color.parseColor("#000000") : Color
				.parseColor("#818084"));
	}

	/*----------------------------------------- Calculo de Constantes --------------------------------*/

	private double factorBIT(EdadVO pEdad, ProfesionVO pProfesion) {
		if (CotizadorUtil.getInstance().isDoPFT()) {
			return pEdad.getBit() * pProfesion.getInvalidez() / 100;
		}
		return pEdad.getBitn() * pProfesion.getInvalidez() / 100;
	}

	/*-----------------------------------  Calculo de Edades  ----------------------------------------*/
	private int getEdadCalculo(int pEdad, int pSexo, boolean pFuma) {
		return Math.max((pEdad - ((pSexo == FEMENINO) ? 3 : 0) - ((pFuma) ? 0
				: 2)), 15);
	}

	private EdadVO getEdadCalculoVO(int pEdad, int pSexo, boolean pFuma) {
		List<EdadVO> edades;
		EdadVO selectedEdadVO = null;
		int searchEdad = Math
				.max((pEdad - ((pSexo == FEMENINO) ? 3 : 0) - ((pFuma) ? 0 : 2)),
						15);
		CommonDAO<EdadVO> polizaDS = new CommonDAO<EdadVO>(caller,
				EdadVO.class, SipacOpenHelper.TABLE_EDADES);
		// ToDo perform search algorithm
		polizaDS.open();
		edades = polizaDS.getFilteredItems("edad = " + searchEdad);
		polizaDS.close();

		if (!edades.isEmpty()) {
			selectedEdadVO = edades.get(0);
		}

		return selectedEdadVO;
	}

	private EdadVO getEdadVO(int pEdad) {
		List<EdadVO> edades;
		EdadVO selectedEdadVO = null;
		CommonDAO<EdadVO> polizaDS = new CommonDAO<EdadVO>(caller,
				EdadVO.class, SipacOpenHelper.TABLE_EDADES);
		// ToDo perform search algorithm
		polizaDS.open();
		edades = polizaDS.getFilteredItems("edad = " + pEdad);
		polizaDS.close();

		if (!edades.isEmpty()) {
			selectedEdadVO = edades.get(0);
		}

		return selectedEdadVO;
	}

	/*-------------------------------------------  Setup's ---------------------------------------------*/

	private void setUpSexo() {
		ArrayList<ValueVO<Integer>> values = new ArrayList<ValueVO<Integer>>();
		values.add(new ValueVO<Integer>(new Integer(MASCULINO), "Masculino"));
		values.add(new ValueVO<Integer>(new Integer(FEMENINO), "Femenino"));
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

	private void setUpPago() {
		ArrayList<ValueVO<Integer>> values = new ArrayList<ValueVO<Integer>>();
		values.add(new ValueVO<Integer>(1, "Anual"));
		values.add(new ValueVO<Integer>(12, "Mensual"));
		values.add(new ValueVO<Integer>(24, "Quincenal"));
		ArrayAdapter<ValueVO<Integer>> dataAdapter = new ArrayAdapter<ValueVO<Integer>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		pagoSpinner.setAdapter(dataAdapter);
		selectedPago = values.get(0);
	}

	private void setUpCalculo() {
		ArrayList<ValueVO<Integer>> values = new ArrayList<ValueVO<Integer>>();
		values.add(new ValueVO<Integer>(1, "Suma Asegurada"));
		values.add(new ValueVO<Integer>(0, "Prima Anual"));
		ArrayAdapter<ValueVO<Integer>> dataAdapter = new ArrayAdapter<ValueVO<Integer>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		calculoSpinner.setAdapter(dataAdapter);
		selectedCalculo = values.get(0);
	}

	@SuppressLint("UseValueOf")
	private void setUpHijos() {
		ArrayList<ValueVO<Double>> values = new ArrayList<ValueVO<Double>>();
		values.add(new ValueVO<Double>(new Double(0.0),
				"Sin Gastos Funerarios Hijos (GFH)"));
		values.add(new ValueVO<Double>(new Double(1.360),
				"Gastos Funerarios(GFH): 1 Hijo"));
		values.add(new ValueVO<Double>(new Double(2.760),
				"Gastos Funerarios Hijos(GFH): 2 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"Gastos Funerarios Hijos(GFH): 3 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"Gastos Funerarios Hijos(GFH): 4 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"Gastos Funerarios Hijos(GFH): 5 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"Gastos Funerarios Hijos(GFH): 6 Hijos"));
		values.add(new ValueVO<Double>(new Double(4.090),
				"Gastos Funerarios Hijos(GFH): 7 Hijos"));

		ArrayAdapter<ValueVO<Double>> dataAdapter = new ArrayAdapter<ValueVO<Double>>(
				caller, android.R.layout.simple_spinner_item, values);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gfhSpinner.setAdapter(dataAdapter);
		selectedGFH = values.get(0);

	}

	private void setUpProfesiones() {
		List<ProfesionVO> profesiones;

		CommonDAO<ProfesionVO> polizaDS = new CommonDAO<ProfesionVO>(caller,
				ProfesionVO.class, SipacOpenHelper.TABLE_PROFESIONES);
		// ToDo perform search algorithm
		polizaDS.open();
		profesiones = polizaDS.getAllItems();
		polizaDS.close();

		ArrayAdapter<ProfesionVO> dataAdapter = new ArrayAdapter<ProfesionVO>(
				caller, android.R.layout.simple_spinner_item, profesiones);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		profesiones.add(0, new ProfesionVO("Ninguno", 0, 1, 1));
		profesionesSpinner.setAdapter(dataAdapter);
		selectedProfesion = profesiones.get(0);
	}

	private double getSumaAsegurada(EdadVO pEdad, ProfesionVO pProfesion,
			EdadVO pEdadConyuge, double pPrimaTotal, int pSexo,
			EdadVO pEdadReal, EdadVO pEdadCom, EdadVO pEdadCom1,
			EdadVO pEdadCom2, EdadVO pEdadCom3, int pSexoC1, int pSexoC2,
			int pSexoC3) throws GEOmmittedError, OverFlowError {
		double numerador = 0, denominador = 0;
		do {
			numerador = pPrimaTotal * 1000;
			denominador = (dobas ? CotizadorUtil.getInstance().getBASFactor(
					pEdad, pProfesion) : 0)
					+ (docii ? CotizadorUtil.getInstance().getCIIFactor(pEdad,
							pProfesion) : 0)
					+ (docma ? CotizadorUtil.getInstance().getCMAFactor(pEdad,
							pProfesion) : 0)
					+ (dotiba ? CotizadorUtil.getInstance().getTIBAFactor(
							pEdad, pProfesion) : 0)
					+ (docat ? CotizadorUtil.getInstance().getCATFactor(
							pEdadReal, pSexo) : 0)
					+ (dogfa ? (double) (CotizadorUtil.getInstance()
							.getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35
							: 1))
							: 0)
					+ (doge ? CotizadorUtil.getInstance().getGEFactor(pEdad,
							pProfesion) : 0)
					+ (dobacy ? (double) (CotizadorUtil.getInstance()
							.getBACYFactor(pEdadConyuge) * (dogfc ? .65 : 1))
							: 0)
					+ (dogfc ? (double) (CotizadorUtil.getInstance()
							.getBACYFactor(pEdadConyuge) * .35) : 0)
					+ (dogpc ? CotizadorUtil.getInstance().getCATFactor(
							pEdadConyuge,
							(pSexo == FEMENINO) ? MASCULINO : FEMENINO) : 0)
					+ (double) (CotizadorUtil.getInstance().getGFHFactor(
							selectedGFH) * (.35))
					+ (doccomp ? CotizadorUtil.getInstance().getBACYFactor(
							pEdadCom) : 0)
					+ (docatcomp1 ? CotizadorUtil.getInstance().getCATFactor(
							pEdadCom1, pSexoC1) : 0)
					+ (docatcomp2 ? CotizadorUtil.getInstance().getCATFactor(
							pEdadCom2, pSexoC2) : 0)
					+ (docatcomp3 ? CotizadorUtil.getInstance().getCATFactor(
							pEdadCom3, pSexoC3) : 0);

			if (dobit) {
				denominador += ((dobas ? CotizadorUtil.getInstance()
						.getBASFactor(pEdad, pProfesion) : 0)
						+ (docat ? CotizadorUtil.getInstance().getCATFactor(
								pEdadReal, pSexo) : 0)
						+ (dogfa ? (double) (CotizadorUtil.getInstance()
								.getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35
								: 1))
								: 0)
						+ (doge ? CotizadorUtil.getInstance().getGEFactor(
								pEdad, pProfesion) : 0)
						+ (dobacy ? (double) (CotizadorUtil.getInstance()
								.getBACYFactor(pEdadConyuge))
								* (dogfc ? .65 : 1) : 0)
						+ (dogfc ? (double) (CotizadorUtil.getInstance()
								.getBACYFactor(pEdadConyuge) * (.35)) : 0)
						+ (dogpc ? CotizadorUtil.getInstance().getCATFactor(
								pEdadConyuge,
								(pSexo == FEMENINO) ? MASCULINO : FEMENINO) : 0)
						+ (double) (CotizadorUtil.getInstance().getGFHFactor(
								selectedGFH) * (.35))
						+ (doccomp ? CotizadorUtil.getInstance().getBACYFactor(
								pEdadCom) : 0)
						+ (docatcomp1 ? CotizadorUtil.getInstance()
								.getCATFactor(pEdadCom1, pSexoC1) : 0)
						+ (docatcomp2 ? CotizadorUtil.getInstance()
								.getCATFactor(pEdadCom2, pSexoC2) : 0) + (docatcomp3 ? CotizadorUtil
						.getInstance().getCATFactor(pEdadCom3, pSexoC3) : 0))
						* factorBIT(pEdad, pProfesion);
			}

			discounts.reset();
			if(dobas && discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_BAS ) ){
				if(discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_BAS ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion) * discounts.getLowerLimit() );
					denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBASFactor(pEdad, pProfesion);	
				}
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_BAS);
			}
			
			if(docat && (discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CAT ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CAT ))){
				if(discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CAT )){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) * discounts.getLimiteCAT() );
				}else if(discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CAT ) && doNcotizacion ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadReal, pSexo);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CAT);
			}
			
			if( dogfa && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFA ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFA ) ) ){
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFA ) ){
					numerador -=  ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion)  * discounts.getLimiteGFA() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFA ) ){
					numerador -=  ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion)  * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBASFFactor(pEdad, pProfesion) * (doNcotizacion ? .35 : 1));
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_GFA);
				
			}
			
			if( docii && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CII ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CII ) )){
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CII ) ){
					numerador -= (CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion) * discounts.getLimiteCII() );			
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CII ) ){
					numerador -= (CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion) * discounts.getLowerLimit() );					
				}
				denominador -= CotizadorUtil.getInstance().getCIIFactor(pEdad, pProfesion);	
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CII);
			}
			
			if( docma && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CMA ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CMA ) ) ) {
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CMA ) ){
					numerador -= (CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion) * discounts.getLimiteCMA() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CMA ) ){
					numerador -= (CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion) * discounts.getLowerLimit() );							
				}
				denominador -= CotizadorUtil.getInstance().getCMAFactor(pEdad, pProfesion);		
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CMA);
			}
			
			if( dotiba && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_TIBA ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_TIBA ) )){
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_TIBA ) ){
					numerador -= (CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion) * discounts.getLimiteTIBA() );					
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_TIBA ) ){
					numerador -= (CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion) * discounts.getLowerLimit() );
				}
				denominador -= CotizadorUtil.getInstance().getTIBAFactor(pEdad, pProfesion);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_TIBA);
			}
			
			if( dobacy && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_BACY ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_BACY ) ) ){
				if ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_BACY ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteBACY() );				
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_BACY ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteBACY() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (dogfc ? .65 : 1));
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_BACY);
			}
			
			if( dogfc && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFC ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFC ) ) ){
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFC ) ){
					numerador -= (( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLimiteGFC() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFC ) ){
					numerador -= (( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getBACYFactor(pEdadConyuge) * (.35) );
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_GFC);
			}
			
			if( dogfh && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFH ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFH ) ) ){ 
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_GFH ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * discounts.getLimiteGFH();
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_GFH ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * discounts.getLowerLimit();
				}				
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getGFHFactor(selectedGFH) * (.35));
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_GFH);
			}
			
			if( dogpc && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATC ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATC ) ) ){ 
				if ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATC ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ((CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO)) * discounts.getLimiteCATC() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATC ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ((CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO)) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadConyuge, (pSexo == CotizadorUtil.FEMENINO) ? CotizadorUtil.MASCULINO : CotizadorUtil.FEMENINO));
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CATC);
			}
			
			if( doccomp && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CCOM ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CCOM ) ) ) {
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CCOM ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ( CotizadorUtil.getInstance().getBACYFactor(pEdadCom) * discounts.getLimiteCCOM() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CCOM ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * ( CotizadorUtil.getInstance().getBACYFactor(pEdadCom) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getBACYFactor(pEdadCom);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CCOM);
			}
			
			if( docatcomp1 && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM1 ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM1 ) ) ){ 
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM1 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo) * discounts.getLimiteCATCOM1() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM1 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom1, pSexo);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CATCOM1);
			}
			
			if( docatcomp2 && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM2 ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM2 ) ) ){
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM2 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo) * discounts.getLimiteCATCOM2() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM2 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo) * discounts.getLowerLimit() );
				}				
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom2, pSexo);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CATCOM2);
			}
			
			if( docatcomp3 && ( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM3 ) || discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM3 ) ) ){ 
				if( discounts.isActivoOverflow( OverflowHandler.OVERFLOW_CATCOM3 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo) * discounts.getLimiteCATCOM3() );
				}else if( discounts.isActivoLowerOverflow( OverflowHandler.OVERFLOW_CATCOM3 ) ){
					numerador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * (CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo) * discounts.getLowerLimit() );
				}
				denominador -= ( dobit ? (1 + factorBIT(pEdad, pProfesion)) : 1 ) * CotizadorUtil.getInstance().getCATFactor(pEdadCom3, pSexo);
				discounts.hacerDescuento(OverflowHandler.OVERFLOW_CATCOM3);
			}

			discounts.checkDescuentos(numerador / denominador);
			discounts.checkLowerDescuentos(numerador / denominador);
			if ((numerador / denominador > 100000000)) {
				throw new OverFlowError();
			}
			try {
				if ((edad < 18 && ((long) (numerador / denominador) > 3000000))
						|| (edad >= 18 && ((long) (numerador / denominador) > 100000000))) {
					Toast.makeText(caller, "No se encontro una solución.",
							Toast.LENGTH_SHORT).show();
					primaTotalInput.setTextColor(Color.RED);
					return 0.0;
				}
			} catch (Exception e) {
				Toast.makeText(caller, "Esta pasando algo raro.",
						Toast.LENGTH_SHORT).show();
			}
		} while (!discounts.isValid());
		if ((edad < 18 && ((dobas ? numerador / denominador : 0)
				+ (doge ? numerador / denominador : 0) + (dogfa ? Math.min(
				numerador / denominador * .35, 180000) : 0)) > MAX)) {
			Toast.makeText(caller,
					"La suma de BAS + GE + GFA pasan los 3'000,000.00.",
					Toast.LENGTH_LONG).show();
			primaTotalInput.setTextColor(Color.RED);
			return 0.0;
		}
		primaTotalInput.setTextColor(Color.BLACK);
		return numerador / denominador;
	}

	/*-------------------------------------- DelayedInitter ------------------------------------------------------*/

	class CotizadorInflateTask extends AsyncTask<String, String, String> {

		private DelayedInitter initter;
		private View generatedView;

		public CotizadorInflateTask(DelayedInitter initter) {
			this.initter = initter;

		}

		protected String doInBackground(String... key) {
			try {
				Thread.sleep(1500);
				generatedView = initter.inProcess();
			} catch (Exception error) {

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

	/*-------------------------------------- Clases ---------------------------------------------------------------*/
	public class OverflowHandler {
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

		public OverflowHandler(double limiteCAT, double limiteGFA,
				double limiteBACY, double limiteGFC, double limiteCATC,
				double limiteGFH, double limiteCCOM, double limiteCATCOM1,
				double limiteCATCOM2, double limiteCATCOM3, double limiteCMA,
				double limiteTIBA, double limiteCII, double limiteBAS,
				double limiteGE) {
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

		public void setLimiteCMA(double limiteCMA) {
			this.limiteCMA = limiteCMA;
		}

		public void setLimiteTIBA(double limiteTIBA) {
			this.limiteTIBA = limiteTIBA;
		}

		public void setLimiteCII(double limiteCII) {
			this.limiteCII = limiteCII;
		}

		public double getLimiteCAT() {
			return limiteCAT;
		}

		public double getLimiteGFA() {
			return limiteGFA;
		}

		public double getLimiteBACY() {
			return limiteBACY;
		}

		public double getLimiteGFC() {
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

		public double getLimiteCII() {
			return limiteCII;
		}

		public double getLowerLimit() {
			return lowerLimit;
		}

		public void checkLowerDescuentos(double pSuma) throws GEOmmittedError,
				OverFlowError {
			lowerOverflow = 0;
			if ((pSuma * (doNcotizacion ? .35 : 1)) < lowerLimit && dogfa
					&& doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_GFA);
			} else {
				desactivarLowerOverflow(OVERFLOW_GFA);
			}

			if ((pSuma * .35) < lowerLimit && dogfc && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_GFC);
			} else {
				desactivarLowerOverflow(OVERFLOW_GFC);
			}

			if ((pSuma * .35) < lowerLimit && dogfh && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_GFH);
			} else {
				desactivarLowerOverflow(OVERFLOW_GFH);
			}

			if (pSuma < lowerLimit && docat && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CAT);
			} else {
				desactivarLowerOverflow(OVERFLOW_CAT);
			}

			if ((pSuma * (dogfc ? .65 : 1)) < lowerLimit && dobacy
					&& doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_BACY);
			} else {
				desactivarLowerOverflow(OVERFLOW_BACY);
			}

			if (pSuma < lowerLimit && dogpc && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CATC);
			} else {
				desactivarLowerOverflow(OVERFLOW_CATC);
			}

			if (pSuma < lowerLimit && doccomp && doNcotizacion
					&& ccompEdad < 18) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CCOM);
			} else {
				desactivarLowerOverflow(OVERFLOW_CCOM);
			}

			if (pSuma < lowerLimit && docatcomp1 && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CATCOM1);
			} else {
				desactivarLowerOverflow(OVERFLOW_CATCOM1);
			}

			if (pSuma < lowerLimit && docatcomp2 && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CATCOM2);
			} else {
				desactivarLowerOverflow(OVERFLOW_CATCOM2);
			}

			if (pSuma < lowerLimit && docatcomp3 && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CATCOM3);
			} else {
				desactivarLowerOverflow(OVERFLOW_CATCOM3);
			}

			if (pSuma < lowerLimit && docma && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CMA);
			} else {
				desactivarLowerOverflow(OVERFLOW_CMA);
			}

			if (pSuma < lowerLimit && dotiba && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_TIBA);
			} else {
				desactivarLowerOverflow(OVERFLOW_TIBA);
			}

			if (pSuma < lowerLimit && docii && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_CII);
			} else {
				desactivarLowerOverflow(OVERFLOW_CII);
			}

			if (pSuma < lowerLimit && dobas && doNcotizacion) {
				if (doge == false) {
					throw new GEOmmittedError();
				}
				activarLowerOverflow(OVERFLOW_BAS);
			} else {
				desactivarLowerOverflow(OVERFLOW_BAS);
			}
		}

		public void checkDescuentos(double pSuma) {
			descuentos = 0;

			if ((pSuma * (doNcotizacion ? .35 : 1)) > limiteGFA && dogfa) {
				activarOverFlow(OVERFLOW_GFA);
			} else {
				desactivarOverFlow(OVERFLOW_GFA);
			}

			if ((pSuma * .35) > limiteGFC && dogfc) {
				activarOverFlow(OVERFLOW_GFC);
			} else {
				desactivarOverFlow(OVERFLOW_GFC);
			}

			if ((pSuma * .35) > limiteGFH && dogfh) {
				activarOverFlow(OVERFLOW_GFH);
			} else {
				desactivarOverFlow(OVERFLOW_GFH);
			}

			if (pSuma > limiteCAT && docat) {
				activarOverFlow(OVERFLOW_CAT);
			} else {
				desactivarOverFlow(OVERFLOW_CAT);
			}

			if ((pSuma * (dogfc ? .65 : 1)) > limiteBACY && dobacy) {
				activarOverFlow(OVERFLOW_BACY);
			} else {
				desactivarOverFlow(OVERFLOW_BACY);
			}

			if (pSuma > limiteCATC && dogpc) {
				activarOverFlow(OVERFLOW_CATC);
			} else {
				desactivarOverFlow(OVERFLOW_CATC);
			}

			if (pSuma > limiteCCOM && doccomp && ccompEdad < 18) {
				activarOverFlow(OVERFLOW_CCOM);
			} else {
				desactivarOverFlow(OVERFLOW_CCOM);
			}

			if (pSuma > limiteCATCOM1 && docatcomp1) {
				activarOverFlow(OVERFLOW_CATCOM1);
			} else {
				desactivarOverFlow(OVERFLOW_CATCOM1);
			}

			if (pSuma > limiteCATCOM2 && docatcomp2) {
				activarOverFlow(OVERFLOW_CATCOM2);
			} else {
				desactivarOverFlow(OVERFLOW_CATCOM2);
			}

			if (pSuma > limiteCATCOM3 && docatcomp3) {
				activarOverFlow(OVERFLOW_CATCOM3);
			} else {
				desactivarOverFlow(OVERFLOW_CATCOM3);
			}

			if (pSuma > limiteCMA && docma) {
				activarOverFlow(OVERFLOW_CMA);
			} else {
				desactivarOverFlow(OVERFLOW_CMA);
			}

			if (pSuma > limiteTIBA && dotiba) {
				activarOverFlow(OVERFLOW_TIBA);
			} else {
				desactivarOverFlow(OVERFLOW_TIBA);
			}

			if (pSuma > limiteCII && docii) {
				activarOverFlow(OVERFLOW_CII);
			} else {
				desactivarOverFlow(OVERFLOW_CII);
			}
		}

		public void resetAll() {
			descuentos = descuentosHechos = 0;
		}

		public void reset() {
			descuentosHechos = 0;
		}

		private void activarLowerOverflow(int constante) {
			lowerOverflow = (lowerOverflow | constante);
		}

		private void desactivarLowerOverflow(int constante) {
			lowerOverflow = (~constante & lowerOverflow);
		}

		public boolean isActivoLowerOverflow(int bandera) {
			return ((lowerOverflow & bandera) == bandera);
		}

		private void hacerDescuento(int constante) {
			descuentosHechos = (descuentosHechos | constante);
		}

		private void activarOverFlow(int constante) {
			descuentos = (descuentos | constante);
		}

		private void desactivarOverFlow(int constante) {
			descuentos = (~constante & descuentos);
		}

		public boolean isActivoOverflow(int bandera) {
			return ((descuentos & bandera) == bandera);
		}

		public boolean isValid() {
			return ((~descuentosHechos & (descuentos | lowerOverflow)) == 0);
		}

	}

}
