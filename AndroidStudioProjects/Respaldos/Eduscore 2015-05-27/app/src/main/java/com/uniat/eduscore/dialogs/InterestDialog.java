package com.uniat.eduscore.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.uniat.eduscore.db.CommonDAO;
import com.uniat.eduscore.db.EduscoreOpenHelper;
import com.uniat.eduscore.util.DismissDialog;
import com.uniat.eduscore.vo.CareerTypeVO;
import com.uniat.eduscore.vo.CareerVO;
import com.university3dmx.eduscore.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 10/04/2015.
 */
public class InterestDialog extends DismissDialog {

    private TextView textInterest;
    private Button btnAcep;
    private String[] typesCareers;
    private String[] idTypesCareers;
    private ViewGroup groupInterest;

    public InterestDialog(Context context, TextView textInterest){
        super(context);
        this.textInterest = textInterest;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_interest);
        init();
        setListeners();
        generateListInterest();
    }

    private void init(){
        btnAcep = (Button) this.findViewById(R.id.btnAceptar);
        idTypesCareers = getIdTypeCareers();
        typesCareers = getTypeCareers();
        groupInterest = (ViewGroup) this.findViewById(R.id.interesesContainer);
    }

    private void generateListInterest(){
        for( int i = 0; i < typesCareers.length; i++ ){
            final ViewGroup title = (ViewGroup) LayoutInflater.from(getContext()).inflate(
                    R.layout.item_title_interest, groupInterest, false);
            TextView textTitle = (TextView) title.findViewById(R.id.TextViewTitleInterest);
            textTitle.setText(typesCareers[i]);
            String[] intereses = getInterests(idTypesCareers[i]);
            groupInterest.addView(title);
            for(int j = 0; j < intereses.length; j++){
                final ViewGroup newInterest = (ViewGroup) LayoutInflater.from(getContext()).inflate(
                        R.layout.item_interest, groupInterest, false);
                final ImageButton imgBtnCheck = (ImageButton) newInterest.findViewById(R.id.chekInterest);
                TextView textInterest = (TextView) newInterest.findViewById(R.id.TextViewInterest);
                textInterest.setText(intereses[j]);
                imgBtnCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgBtnCheck.setImageResource( R.drawable.selector_check_interest_on);
                    }
                });
                groupInterest.addView(newInterest);
            }

        }
    }

    private void setListeners(){
        btnAcep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        View dialogPicker = findViewById(R.id.dialogPicker);

        super.dismiss(dialogPicker);
    }

    private String[] getTypeCareers(){
        List<CareerTypeVO> careerTypeVO = new ArrayList<>();
        CommonDAO<CareerTypeVO> careerTypeDAO = new CommonDAO<>(getContext(), CareerTypeVO.class, EduscoreOpenHelper.TABLE_ESC_CAREERTYPE);
        careerTypeDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_ESC_CAREERTYPE + " WHERE status = '1' GROUP BY name" ;
        careerTypeVO = careerTypeDAO.runQuery( sql );
        String[] types = new String[careerTypeVO.size()];
        for (int i = 0; i < types.length; i++){
            //El campo status esta cambiado por name
            types[i] = careerTypeVO.get(i).getName();
        }
        return types;
    }

    private String[] getIdTypeCareers(){
        List<CareerTypeVO> careerTypeVO = new ArrayList<>();
        CommonDAO<CareerTypeVO> careerTypeDAO = new CommonDAO<>(getContext(), CareerTypeVO.class, EduscoreOpenHelper.TABLE_ESC_CAREERTYPE);
        careerTypeDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_ESC_CAREERTYPE + " WHERE status = '1' GROUP BY name" ;
        careerTypeVO = careerTypeDAO.runQuery( sql );
        String[] types = new String[careerTypeVO.size()];
        for (int i = 0; i < types.length;i++){
            //El campo Name esta cambiado por idCarerrType
            types[i] = careerTypeVO.get(i).getIdCareerType();
        }
        return types;
    }

    private String[] getInterests(String type){
        List<CareerVO> materiasVO = new ArrayList<>();
        CommonDAO<CareerVO> careerDAO = new CommonDAO<>(getContext(), CareerVO.class, EduscoreOpenHelper.TABLE_ESC_CAREER);
        careerDAO.open();
        String sql = "SELECT * FROM " + EduscoreOpenHelper.TABLE_ESC_CAREER + " WHERE idCareerType = '" + type + "' AND prefix = '1'";
        materiasVO = careerDAO.runQuery( sql );
        String[] materias = new String[materiasVO.size()];
        for (int i = 0; i < materias.length; i++){
            materias[i] = materiasVO.get(i).getName();
        }
        return materias;
    }
}
