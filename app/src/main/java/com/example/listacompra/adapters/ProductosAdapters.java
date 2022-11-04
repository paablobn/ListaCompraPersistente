package com.example.listacompra.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompra.R;
import com.example.listacompra.modelos.Producto;

import java.util.List;

public class ProductosAdapters extends RecyclerView.Adapter<ProductosAdapters.ProductoVH> {

    private List<Producto> objects;
    private int resource;
    private Context context;

    public ProductosAdapters(List<Producto> objects, int resource, Context context) {
        this.objects = objects;
        this.resource = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoView = LayoutInflater.from(context).inflate(resource, null);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        productoView.setLayoutParams(layoutParams);
        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoVH holder, int position) {
        Producto producto = objects.get(position);
        holder.lbProducto.setText(producto.getNombre());
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objects.remove(producto);
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int cantidad;
                try {
                    cantidad = Integer.parseInt(editable.toString());
                }catch (NumberFormatException ex){
                    cantidad = 0;
                }
                producto.setCantidad(cantidad);
                producto.updateTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ProductoVH extends RecyclerView.ViewHolder {


        TextView lbProducto;
        EditText txtCantidad;
        ImageButton btnEliminar;

        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            lbProducto = itemView.findViewById(R.id.txtNombreProductoViewHolder);
            txtCantidad = itemView.findViewById(R.id.txtCantidadProductoViewHolder);
            btnEliminar = itemView.findViewById(R.id.btnEliminarProductoViewHolder);
        }
    }
}
