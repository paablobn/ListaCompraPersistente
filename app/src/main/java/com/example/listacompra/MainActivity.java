package com.example.listacompra;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.listacompra.adapters.ProductosAdapters;
import com.example.listacompra.modelos.Constantes;
import com.example.listacompra.modelos.Producto;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.listacompra.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> productos;

    private ProductosAdapters adapter;
    private RecyclerView.LayoutManager layoutManager;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        //Orientacion del movil
        //getResources().getConfiguration().orientation
        //PORTRAIT --> VERTICAL
        //LANDSCAPE --> HORIZONTAL

        int columnas;
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;

        productos = new ArrayList<>();
        adapter = new ProductosAdapters(productos, R.layout.producto_view_holder, this);
        layoutManager = new GridLayoutManager(this, columnas);
        binding.conteinMain.Contenedor.setAdapter(adapter);
        binding.conteinMain.Contenedor.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.LISTA, MODE_PRIVATE);

        cargarDatos();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto().show();
            }
        });
    }

    private void cargarDatos() {
        if (sharedPreferences.contains(Constantes.LISTAPERSISTENTE)) {
            String json = sharedPreferences.getString(Constantes.LISTAPERSISTENTE, "");
            Type type = new TypeToken<ArrayList<Producto>>() {
            }.getType();
            List<Producto> listProductos = new Gson().fromJson(json, type);
            productos.addAll(listProductos);
        }
    }

    private AlertDialog createProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.create_title));
        builder.setCancelable(false);

        View productoView = LayoutInflater.from(this).inflate(R.layout.producto_view_alert, null);
        EditText txtNombre = productoView.findViewById(R.id.txtNombreProductoAlert);
        EditText txtCantidad = productoView.findViewById(R.id.txtCantidadProductoAlert);
        EditText txtPrecio = productoView.findViewById(R.id.txtPrecioproductoAlert);
        TextView lbTotal = productoView.findViewById(R.id.lbTotalProductoAlert);
        builder.setView(productoView);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float precio = Float.parseFloat(txtPrecio.getText().toString());
                    //Formatear numerows en cadenas
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    lbTotal.setText(numberFormat.format(cantidad * precio));
                } catch (NumberFormatException ex) {
                }

            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setNegativeButton(getResources().getString(R.string.btn_negative_), null);
        builder.setPositiveButton(getResources().getString(R.string.btn_positive_create), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    Producto producto = new Producto(
                            txtNombre.getText().toString(),
                            Integer.parseInt(txtCantidad.getText().toString()),
                            Float.parseFloat(txtPrecio.getText().toString())
                    );
                    productos.add(0, producto);
                    adapter.notifyItemInserted(0);
                    guardar();
                }
            }
        });
        return builder.create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        guardar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        guardar();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("PRODUCTOS", productos);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> temp = (ArrayList<Producto>) savedInstanceState.getSerializable("PRODUCTOS");
        productos.addAll(temp);
        adapter.notifyItemRangeInserted(0, productos.size());
    }

    public void guardar() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.LISTA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = new Gson().toJson(productos);
        editor.putString(Constantes.LISTAPERSISTENTE, json);
        editor.apply();
    }
}