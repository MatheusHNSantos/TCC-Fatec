package util.viacep;

import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViaCEP {

    private boolean canRun = false;

    public static Endereco buscarCep(String cep) {

        String json;

        Endereco endereco = null;

        try {
            URL url = new URL("http://viacep.com.br/ws/" + cep + "/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            StringBuilder jsonSb = new StringBuilder();

            br.lines().forEach(l -> jsonSb.append(l.trim()));

            json = jsonSb.toString();

            Map<String, String> mapa = new HashMap<>();

            Matcher matcher = Pattern.compile("\"\\D.*?\": \".*?\"").matcher(json);
            while (matcher.find()) {
                String[] group = matcher.group().split(":");
                mapa.put(group[0].replaceAll("\"", "").trim(), group[1].replaceAll("\"", "").trim());
            }

            endereco = new Endereco();

            endereco.setRua(mapa.get("logradouro"));
            endereco.setBairro(mapa.get("bairro"));

            //region JSON FORMAT
            /*
                Json Obj format

                "cep": "13848-111",
                "logradouro": "Rua Edmundo Franco de Campos",
                "complemento": "",
                "bairro": "Jardim Novo II",
                "localidade": "Mogi Guaçu",
                "uf": "SP",
                "unidade": "",
                "ibge": "3530706",
                "gia": "4558"
            */
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
        }

        return endereco;
    }


}






