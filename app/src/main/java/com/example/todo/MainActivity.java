package com.example.todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Task> taskList;
    private TaskAdapter taskAdapter;
    public RecyclerView recyclerView;
    private EditText titleInput, descriptionInput;
    Button addButton;

    private static final String NOME_PREFS = "preferencias_tarefas";
    private static final String CHAVE_LISTA_TAREFAS = "chave_lista_tarefas";

    @SuppressLint({"NotifyDataSetChanged", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = loadTask();
        taskAdapter = new TaskAdapter(taskList, this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();

            if (title.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
                Task task = new Task(title, false);

                taskList.add(task);
                taskAdapter.notifyDataSetChanged();
                salvarTarefas();

                Toast.makeText(this, "Tarefa adicionada", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void salvarTarefas() {

        SharedPreferences sharedpreferences = getSharedPreferences(NOME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(taskList);
        editor.putString(CHAVE_LISTA_TAREFAS, json);

        for (int i = 0; i < taskList.size(); i++) {
            editor.putBoolean("Tarefa_" + i + "_Completada", taskList.get(i).isCompleted());
        }

        editor.apply();
    }

    private ArrayList<Task> loadTask() {
        SharedPreferences sharedpreferences = getSharedPreferences(NOME_PREFS, Context.MODE_PRIVATE);
        String json = sharedpreferences.getString(CHAVE_LISTA_TAREFAS, null);

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Task>>() {
        }.getType();

        // Converte o JSON para a lista de tarefas
        ArrayList<Task> taskList = gson.fromJson(json, type);

        // Se a lista estiver vazia (não há tarefas no SharedPreferences), inicializa a lista
        if (taskList == null) {
            taskList = new ArrayList<>();
        }

        // Atualiza o status de "concluída" de cada tarefa
        for (int i = 0; i < taskList.size(); i++) {
            boolean isCompleted = sharedpreferences.getBoolean("Tarefa_" + i + "_Completada", false);
            taskList.get(i).setCompleted(isCompleted);
        }

        return taskList; // Retorna a lista carregada
    }

}


