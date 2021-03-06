package com.android.appcompose.composable.utility.cardgrid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.appcompose.BR;
import com.android.appcompose.R;

import com.android.appcompose.composable.utility.cardgrid.model.CardDataModel;
import com.android.appcompose.composable.utility.cardgrid.model.ParentModel;
import com.android.appcompose.database.model.ClassroomModel;
import com.android.appcompose.database.model.MentorModel;
import com.android.appcompose.databinding.CardgridItemBinding;
import com.android.appcompose.databinding.CardgridListItemBinding;
import com.android.appcompose.utils.DataType;

import java.util.ArrayList;


public class CardRecyclerViewAdapter<T> extends RecyclerView.Adapter<CardRecyclerViewAdapter.ViewHolder>  implements OnCardGridItemClickListener {

    //Member variables
    private ArrayList<Object> data = new ArrayList<Object>();

    private Context mContext;
    public CardGridListener listener;
    private DataType type;
    private int orientation;
    /**
     * Constructor that passes in the sports data and the context
     * @param childData ArrayList containing the sports data
     * @param context Context of the application
     */
    public CardRecyclerViewAdapter(Context context, ArrayList<T> childData, DataType type, int orientation,CardGridListener listener) {

        this.mContext = context;
        this.setData((ArrayList<Object>) childData);
        this.type = type;
        this.listener = listener;
        this.orientation = orientation;
    }

    /**
     * Required method for creating the viewholder objects.
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return The newly create ViewHolder.
     */
    @Override
    public CardRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(this.orientation == LinearLayoutManager.VERTICAL){

            CardgridItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.cardgrid_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(binding, this.orientation);
            viewHolder.type = this.type;
            return viewHolder;
        }else{
            CardgridListItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.cardgrid_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(binding, this.orientation);
            viewHolder.type = this.type;
            return viewHolder;
        }

        

    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * Required method that binds the data to the viewholder.
     * @param holder The viewholder into which the data should be put.
     * @param position The adapter position.
     */
    @Override
    public void onBindViewHolder(CardRecyclerViewAdapter.ViewHolder holder, int position) {

        Object dataModel = data.get(position);
        holder.bind(position,dataModel, this.type);
        if(this.orientation == LinearLayoutManager.VERTICAL){
            holder.itemBinding.setItemClickListener(this);
        }else{
            holder.itemListBinding.setItemClickListener(this);
        }


    }



    /**
     * Required method for determining the size of the data set.
     * @return Size of the data set.
     */
    @Override
    public int getItemCount() {
        int totalItems = this.data.size();

        return totalItems;
    }


    public ArrayList<Object> getData() {
        return data;
    }

    public void setData(ArrayList<Object> data) {
        this.data = data;
    }

    @Override
    public void onItemCategoryClicked(ParentModel type) {
        Log.d("ADAPTER","ITEM Type CLICKED");
    }

    @Override
    public void onItemClicked(CardDataModel item) {
        Log.d("ADAPTER","ITEM CLICKED");
        this.listener.onCardClicked(item);
    }

    /**
     * ViewHolder class that represents each row of data in the RecyclerView
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private DataType type;
        private int orientation;
        public CardgridItemBinding itemBinding;
        public CardgridListItemBinding itemListBinding;
        /**
         * Constructor for the ViewHolder, used in onCreateViewHolder().
         * @param itemView The rootview of the list_item.xml layout file
         */
        ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(CardgridItemBinding itemBinding, int orientation) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
            this.orientation = orientation;
        }

        public ViewHolder(CardgridListItemBinding itemBinding, int orientation) {
            super(itemBinding.getRoot());
            this.itemListBinding = itemBinding;
            this.orientation = orientation;
        }

        public void bind(int index, Object obj, DataType type) {

            switch (this.type){
                case FEATURED_CLASSROOMS:
                    ClassroomModel currentSport = (ClassroomModel) obj;
                    if(this.orientation == LinearLayoutManager.VERTICAL){
                        itemBinding.setVariable(BR.model, new CardDataModel(index,currentSport.getName(), currentSport.getAdmin(), DataType.FEATURED_CLASSROOMS));
                    }else{
                        itemListBinding.setVariable(BR.model, new CardDataModel(index,currentSport.getName(), currentSport.getAdmin(), DataType.FEATURED_CLASSROOMS));
                    }


                    break;
                case FEATURED_MENTORS:

                    MentorModel mentor = (MentorModel)obj;
                    String base64EncodedString = mentor.getImage();
                    byte[] imageBytes = Base64.decode(base64EncodedString,Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length);

                    if(this.orientation == LinearLayoutManager.VERTICAL){
                        itemBinding.setVariable(BR.model, new CardDataModel(index,mentor.getName(), "Teacher", DataType.FEATURED_MENTORS));
                        itemBinding.image.setImageBitmap(decodedImage);
                    }else{
                        itemListBinding.setVariable(BR.model, new CardDataModel(index,mentor.getName(), "Teacher", DataType.FEATURED_MENTORS));
                        itemListBinding.image.setImageBitmap(decodedImage);
                    }



                    break;
                default:
                    Log.d("","NA");
            }
            if(this.orientation == LinearLayoutManager.VERTICAL){
                itemBinding.executePendingBindings();
            }else {
                itemListBinding.executePendingBindings();
            }

        }



    }
}