package com.example.listacompra;

import android.content.DialogInterface;
import android.os.Bundle;

import com.example.listacompra.adapters.ProductosAdapters;
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

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> productos;

    private ProductosAdapters adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        productos = new ArrayList<>();
        adapter = new ProductosAdapters(productos, R.layout.producto_view_holder,this);
        layoutManager = new GridLayoutManager(this,1);
        binding.conteinMain.Contenedor.setAdapter(adapter);
        binding.conteinMain.Contenedor.setLayoutManager(layoutManager);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto().show();
            }
        });
    }

    private AlertDialog createProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Nuevo Producto a la lista");
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
                    lbTotal.setText(numberFormat.format(cantidad*precio));
                } catch (NumberFormatException ex) {}

            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("CREAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    Producto producto = new Producto(
                            txtNombre.getText().toString(),
                            Integer.parseInt(txtCantidad.getText().toString()),
                            Float.parseFloat(txtPrecio.getText().toString())
                    );
                    productos.add(0,producto);
                    adapter.notifyItemInserted(0);
                }
            }
        });
        return builder.create();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("PRODUCTOS",productos);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> temp = (ArrayList<Producto>) savedInstanceState.getSerializable("PRODUCTOS");
        productos.addAll(temp);
        adapter.notifyItemRangeInserted(0,productos.size());
    }
}