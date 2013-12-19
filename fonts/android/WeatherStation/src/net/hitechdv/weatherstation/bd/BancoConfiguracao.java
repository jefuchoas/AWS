package net.hitechdv.weatherstation.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.hitechdv.weatherstation.R.raw;

public class BancoConfiguracao extends BancoManager {
	
	public static final String NAME = "aws";
    public static final int VERSAO = 1;
    public static final String TAG_LOG = "BancoAWS";

    public BancoConfiguracao (Context context) {
        //defino pelo contrutor do BancoManager a versão e o nome do banco
        super(context, NAME, VERSAO);
    }
    @Override
    public void onCreate(SQLiteDatabase bd) {
        criaTabelas(bd);
    }

    /**
     * Este método é chamado automaticamente quando a versão é alterada.
     *
     * @param bd
     * @param versaoAtual
     * @param versaoNova
     */
    @Override
    public void onUpgrade(SQLiteDatabase bd, int versaoAtual, int versaoNova) {
        //realiza o tratamento de upgrade, caso tenha 
        //alteração em tabelas por exemplo.
        Log.e(TAG_LOG, "Versão atual: " + versaoAtual);
        Log.e(TAG_LOG, "Nova versão: " + versaoNova);
        //Aqui você deve fazer o tratamento do update do banco.
        //no caso estou apagando minha tabela e criando novamente.
        try {
            bd.execSQL("drop table acesso;");
        } catch (Exception e) {
            Log.e(TAG_LOG, "onUpgrade", e);
        }
        criaTabelas(bd);
    }

    private void criaTabelas(SQLiteDatabase bd) {
        try {
            //crio o banco de dados atravez do arquivo create.
            byFile(raw.create, bd);
        } catch (Exception e) {
            Log.e(TAG_LOG, "criaTabelas", e);
        }        
}
}
