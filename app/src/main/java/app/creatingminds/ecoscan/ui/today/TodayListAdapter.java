package app.creatingminds.ecoscan.ui.today;

/**
 * Created by nhoxbypass
 */

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.creatingminds.ecoscan.R;
import app.creatingminds.ecoscan.data.model.Food;
import app.creatingminds.ecoscan.utils.DateUtils;
import app.creatingminds.ecoscan.utils.FormatUtils;
import app.creatingminds.ecoscan.utils.UiUtils;

// TODO: Improve performance, add ViewHolder pattern
public class TodayListAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<TodayFoodInfoItem> listViewItemList = new ArrayList<TodayFoodInfoItem>();

    // ListViewAdapter의 생성자
    public TodayListAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_food_today, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ivFoodImg = convertView.findViewById(R.id.iv_food_img);
        TextView tvFoodName = convertView.findViewById(R.id.tv_food_name);
        TextView tvExpireDate = convertView.findViewById(R.id.tv_expire_date);
        TextView tvQuantity = convertView.findViewById(R.id.tv_quantity);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TodayFoodInfoItem foodInfoItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ivFoodImg.setImageDrawable(ContextCompat.getDrawable(context, foodInfoItem.getIcon()));
        tvFoodName.setText(FormatUtils.capitalizeWords(foodInfoItem.getTitle()));
        tvExpireDate.setText(String.format("%s days left", foodInfoItem.getExpireDate()));
        tvQuantity.setText(String.valueOf(foodInfoItem.getQuantity()));

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(@DrawableRes int icon, String title, String desc, int quantity) {
        TodayFoodInfoItem item = new TodayFoodInfoItem(icon, title, desc, quantity);
        addItem(item);
    }

    public void addItem(TodayFoodInfoItem infoItem) {
        listViewItemList.add(infoItem);
        notifyDataSetChanged();
    }

    public void addItem(Food food, @DrawableRes int icon, int quantity) {
        listViewItemList.add(new TodayFoodInfoItem(icon, food.getName(),
                FormatUtils.formatDate(food.getExpireTimestamp()), quantity));
        notifyDataSetChanged();
    }

    public void setTodayFood(ArrayList<TodayFoodInfoItem> foodInfoItemList) {
        listViewItemList = foodInfoItemList;
        notifyDataSetChanged();
    }

    // TODO: Support set quantity or move the setter outside
    public void setFood(List<Food> foodList) {
        long currentTimestamp = System.currentTimeMillis();
        Random random = new Random();
        listViewItemList = new ArrayList<>();
        for (int i = 0; i < foodList.size(); i++) {
            Food food = foodList.get(i);
            listViewItemList.add(new TodayFoodInfoItem(UiUtils.getFoodIcon(food.getName()), food.getName(),
                    DateUtils.getDayDiff(currentTimestamp, food.getExpireTimestamp()), random.nextInt(3) + 1));
        }

        notifyDataSetChanged();
    }

    public void updateFood(int i, Food food, @DrawableRes int icon, int quantity) {
        long currentTimestamp = System.currentTimeMillis();
        listViewItemList.set(i, new TodayFoodInfoItem(icon, food.getName(),
                DateUtils.getDayDiff(currentTimestamp, food.getExpireTimestamp()), quantity));
        notifyDataSetChanged();
    }

    public void removeItem(int i) {
        listViewItemList.remove(i);
        notifyDataSetChanged();
    }
}