package com.centura_technologies.mycatalogue.Order.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.centura_technologies.mycatalogue.Order.Controller.Order;
import com.centura_technologies.mycatalogue.Order.Model.BillingProducts;
import com.centura_technologies.mycatalogue.R;
import com.centura_technologies.mycatalogue.Support.DBHelper.DB;
import com.centura_technologies.mycatalogue.Support.DBHelper.DbHelper;
import com.centura_technologies.mycatalogue.Support.DBHelper.StaticData;
import com.centura_technologies.mycatalogue.Support.GenericData;

import java.util.ArrayList;

/**
 * Created by Centura User1 on 24-09-2016.
 */
public class OrderProductViewAdapter extends RecyclerView.Adapter<OrderProductViewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<BillingProducts> data;
    TextView qtydecrement, qtyincrement;
    Button apply, cancel;
    EditText qtytext;
    public static TextView grandtotal;
    Activity a;
    public static Double total_amount = 0.0;
    int viewHeight;

    public OrderProductViewAdapter(Context context) {
        this.mContext = context;
        a = (Activity) mContext;
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.loadOrders();
        this.data = StaticData.orders.get(StaticData.ViewPosition).billingProducts;
        grandtotal = (TextView) a.findViewById(R.id.orderviewamount);
        grandtotal.setText("Total Amount: Rs." + StaticData.orders.get(StaticData.ViewPosition).Amount + "");
       /* if (Order.shortlistedorders)
            this.data = Order.shorlistedmodel;
        else if (Order.selectedcategories) {
            this.data = Order.billingProdu ctsArrayList;
            Order.selectedcategories = false;
        } else
            this.data = DB.getBillprodlist();*/
    }

    public static void clearBill() {
        total_amount = 0.0;
        grandtotal.setText("Rs " + total_amount + "");
    }

    @Override
    public OrderProductViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderlist, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OrderProductViewAdapter.ViewHolder holder, final int position) {
        holder.orderlistlayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        holder.orderlistlayout.setVisibility(View.VISIBLE);
        holder.plusincrement.setVisibility(View.INVISIBLE);
        holder.minusincrement.setVisibility(View.INVISIBLE);
        //Order.orderlist_recyclerview.getLayoutParams().height += viewHeight;
        holder.name.setText(data.get(position).getTitle());
        holder.unit.setText(data.get(position).getWeight() + "");
        holder.qty.setText(data.get(position).getQuantity() + "");
        holder.price.setText(data.get(position).getPrice() + "");
        GenericData.setImage(data.get(position).getImageUrl(), holder.productimage, mContext);
        data.get(position).setAmount(data.get(position).getSellingPrice() * Double.parseDouble(data.get(position).getQuantity() + ""));
        holder.amount.setText(data.get(position).getAmount() + "");
        //onClicks(holder, position);
    }

    private void increment(int position) {
        total_amount += data.get(position).getPrice();
        grandtotal.setText("Rs " + total_amount + "");
    }

    private void decrement(int position) {
        total_amount -= data.get(position).getPrice();
        grandtotal.setText("Rs " + total_amount + "");
    }

    private void onClicks(final ViewHolder holder, final int position) {
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setQuantity(data.get(position).getQuantity() + 1);
                increment(position);
                notifyDataSetChanged();
            }
        });

        holder.unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setQuantity(data.get(position).getQuantity() + 1);
                increment(position);
                notifyDataSetChanged();
            }
        });

        holder.qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_quantity);
                qtydecrement = (TextView) dialog.findViewById(R.id.qtydecrement);
                qtyincrement = (TextView) dialog.findViewById(R.id.qtyincrement);
                qtytext = (EditText) dialog.findViewById(R.id.qtytext);
                apply = (Button) dialog.findViewById(R.id.apply);
                cancel = (Button) dialog.findViewById(R.id.cancel);
                qtytext.setText(data.get(position).getQuantity() + "");
                dialog.show();
                qtydecrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (data.get(position).getQuantity() != 0) {
                            qtytext.setText((Integer.parseInt(qtytext.getText().toString()) - 1) + "");
                        } else
                            Toast.makeText(mContext, "Min Count is 0", Toast.LENGTH_SHORT).show();
                    }
                });
                qtyincrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtytext.setText((Integer.parseInt(qtytext.getText().toString()) + 1) + "");
                    }
                });
                apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //data.get(position).setQuantity(Integer.parseInt(qtytext.getText().toString()));
                        increment(position, Integer.parseInt(qtytext.getText().toString()));
                        dialog.cancel();
                        notifyDataSetChanged();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
            }
        });

        holder.plusincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setQuantity(data.get(position).getQuantity() + 1);
                increment(position);
                notifyDataSetChanged();
            }
        });

        holder.minusincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).getQuantity() != 0) {
                    data.get(position).setQuantity(data.get(position).getQuantity() - 1);
                    decrement(position);
                    notifyDataSetChanged();
                } else
                    Toast.makeText(mContext, "Min Count is 0", Toast.LENGTH_SHORT).show();
            }
        });

        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setQuantity(data.get(position).getQuantity() + 1);
                increment(position);
                notifyDataSetChanged();
            }
        });

        holder.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(position).setQuantity(data.get(position).getQuantity() + 1);
                increment(position);
                notifyDataSetChanged();
            }
        });
    }

    private void increment(int position, int qty) {
        int actualqty = qty;
        if (data.get(position).getQuantity() > qty) {
            qty = qty - data.get(position).getQuantity();
            total_amount += data.get(position).getPrice() * qty;
        } else {
            qty = data.get(position).getQuantity() - qty;
            total_amount -= data.get(position).getPrice() * qty;
        }
        data.get(position).setQuantity(actualqty);
        grandtotal.setText("Rs " + total_amount + "");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, unit, qty, price, amount;
        TextView plusincrement, minusincrement;
        LinearLayout orderlistlayout;
        ImageView productimage;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            unit = (TextView) v.findViewById(R.id.unit);
            qty = (TextView) v.findViewById(R.id.qty);
            price = (TextView) v.findViewById(R.id.price);
            amount = (TextView) v.findViewById(R.id.amount);
            plusincrement = (TextView) v.findViewById(R.id.plusincrement);
            minusincrement = (TextView) v.findViewById(R.id.minusincrement);
            orderlistlayout = (LinearLayout) v.findViewById(R.id.orderlistlayout);
            productimage=(ImageView)v.findViewById(R.id.productimage);
        }
    }
}
