package com.afmobi.palmchat.ui.activity.register;

import java.util.ArrayList;
import java.util.List;

import com.afmobi.palmchat.BaseActivity;
import com.afmobi.palmchat.MyActivityManager;
import com.afmobi.palmchat.PalmchatApp;
import com.afmobi.palmchat.constant.DefaultValueConstant;
import com.afmobi.palmchat.constant.JsonConstant;
import com.afmobi.palmchat.db.DBState;
import com.afmobi.palmchat.log.PalmchatLogUtils;
import com.afmobi.palmchat.ui.activity.profile.MyProfileActivity;
import com.afmobi.palmchat.ui.activity.profile.MyProfileDetailActivity;
import com.afmobi.palmchat.ui.customview.AppDialog;
import com.afmobi.palmchat.ui.customview.MySideBar;
import com.afmobi.palmchat.util.SearchFactory;
import com.afmobi.palmchat.util.SearchFilter;
import com.afmobi.palmchat.util.SharePreferenceUtils;
import com.afmobigroup.gphone.R;
import com.core.AfProfileInfo;
import com.core.Consts;
import com.core.cache.CacheManager;
import com.core.cache.CacheManager.Country;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CountryActivity extends BaseActivity implements OnClickListener {

    private SearchFilter searchFilter = SearchFactory.getSearchFilter(SearchFactory.DEFAULT_CODE);
    private SearchTextWatcher searchTextWatcher = new SearchTextWatcher();
    private ListView mListView;
    private MySideBar mSideBar;
    private List<Country> countries = new ArrayList<Country>();
    private CountryAdapter mAdapter;
    private LayoutInflater inflater;
    private TextView mDialogText;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private boolean isSelectCountryOnly;
    /*来自哪个页面*/
    private String key_from = "";

    @Override
    public void findViews() {
        Intent intent = getIntent();
        if (intent != null) {
            isSelectCountryOnly = intent.getBooleanExtra(JsonConstant.KEY_SELECT_COUNTRY_ONLY, false);
            key_from = intent.getStringExtra(JsonConstant.KEY_FROM);
        }
        setContentView(R.layout.activity_country);
        inflater = LayoutInflater.from(this);

        ((TextView) findViewById(R.id.title_text)).setText(R.string.sign1);
        mListView = (ListView) findViewById(R.id.countrylist);
        initSection();
        getData();
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Country item = (Country) parent.getAdapter().getItem(position);

                if (key_from != null && !key_from.isEmpty() && key_from.equals(MyProfileDetailActivity.class.getSimpleName())) {
                    /*如果是编辑profile时，国家发生了改变，则弹出提示框*/
                    AfProfileInfo cacheProfile = CacheManager.getInstance().getMyProfile();
                    if (cacheProfile.country != null && !cacheProfile.country.equals(item.getCountry())) { //国家发生改变
                        AppDialog appDialog = new AppDialog(context);
                        String msg = getResources().getString(R.string.change_country_notice);
                        appDialog.createConfirmDialog(context, msg, new AppDialog.OnConfirmButtonDialogListener() {
                            @Override
                            public void onRightButtonClick() {

                                Intent intent = new Intent(CountryActivity.this, RegionTwoActivity.class);
                                intent.putExtra(JsonConstant.KEY_COUNTRY, item.getCountry());
                                intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, "+" + item.getCode());
                                intent.putExtra(JsonConstant.KEY_FLAG, false);
                                startActivityForResult(intent, DefaultValueConstant.RESULT_10);
                            }

                            @Override
                            public void onLeftButtonClick() {
                                finish();
                            }
                        });
                        appDialog.show();
                    } else { //国家未发改变
                        Intent intent = new Intent(CountryActivity.this, RegionTwoActivity.class);
                        intent.putExtra(JsonConstant.KEY_COUNTRY, item.getCountry());
                        intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, "+" + item.getCode());
                        intent.putExtra(JsonConstant.KEY_FLAG, false);
                        startActivityForResult(intent, DefaultValueConstant.RESULT_10);
                    }
                } else {
                    if (isSelectCountryOnly) {//只选国家  选完后就返回
                        Intent intent = new Intent();
                        intent.putExtra(JsonConstant.KEY_COUNTRY, item.getCountry());
                        intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, "+" + item.getCode());
                        setResult(Activity.RESULT_OK, intent);
                        onBackPressed();
                    } else {//继续选省市
                        Intent intent = new Intent(CountryActivity.this, RegionTwoActivity.class);
                        intent.putExtra(JsonConstant.KEY_COUNTRY, item.getCountry());
                        intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, "+" + item.getCode());
                        intent.putExtra(JsonConstant.KEY_FLAG, false);
                        startActivityForResult(intent, DefaultValueConstant.RESULT_10);
                    }
                }


            }

        });
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                if (mSideBar != null && mAdapter != null && prefirstVisibleItem != firstVisibleItem) {
                    prefirstVisibleItem = firstVisibleItem;
                    mSideBar.setNowIndex(mAdapter.getSectionForPosition(firstVisibleItem));
                }
            }
        });
        ImageView back_button = (ImageView) findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private int prefirstVisibleItem;

    @Override
    public void init() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == DefaultValueConstant.RESULT_10) {
                Intent intent = new Intent();
                intent.putExtra(JsonConstant.KEY_CITY, data.getStringExtra(JsonConstant.KEY_CITY));
                intent.putExtra(JsonConstant.KEY_STATE, data.getStringExtra(JsonConstant.KEY_STATE));
                intent.putExtra(JsonConstant.KEY_COUNTRY, data.getStringExtra(JsonConstant.KEY_COUNTRY));
                intent.putExtra(JsonConstant.KEY_COUNTRY_CODE, data.getStringExtra(JsonConstant.KEY_COUNTRY_CODE));
                intent.putExtra(JsonConstant.KEY_REGION_LIST, data.getSerializableExtra(JsonConstant.KEY_REGION_LIST));
                setResult(DefaultValueConstant.RESULT_10, intent);
                MyActivityManager.getScreenManager().popActivity();
            }
        }

    }

    private void initSection() {
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.list_position, null);
        mDialogText.setVisibility(View.GONE);
        mSideBar = (MySideBar) findViewById(R.id.sideBar);
        mSideBar.setListView(mListView);
        mSideBar.setTextView(mDialogText);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);

    }

    private void search(String text) {
        String compator = searchFilter.getInputString(text);
        List<Country> temp = new ArrayList<Country>();
        List<Country> list = searchTextWatcher.mmList;
        boolean hasName = true;

        if (hasName) {
            for (Country item : list) {
                String k = searchFilter.getFullSpell(item.getCountry());
                if (k.contains(compator)) {
                    temp.add(item);
                }
            }
        }
        mAdapter.reflashData(temp);
        mAdapter.notifyDataSetChanged();
    }

    public class CountryAdapter extends BaseAdapter implements SectionIndexer {

        private Context mContext;
        List<Country> list;
        private String[] mSections;
        private Integer[] mPositions;

        public CountryAdapter(Context context, List<Country> list) {
            this.mContext = context;
            this.list = list;
            fill();
        }

        private boolean isSame(final String previousSection, final String section) {
            return previousSection != null ? previousSection.equals(section) : false;
        }

        private void fill() {
            String currentSection = null;
            List<String> tempSections = new ArrayList<String>();
            List<Integer> tempPosition = new ArrayList<Integer>();
            final int count = getCount();
            for (int i = 0; i < count; i++) {
                final String section = "" + searchFilter.getAlpha(((Country) list.get(i)).getSortKey());
                if (!isSame(currentSection, section)) {
                    tempSections.add(section);
                    tempPosition.add(i);
                    currentSection = section;
                }
            }
            PalmchatLogUtils.println(tempSections + "\n" + tempPosition);
            mSections = new String[tempSections.size()];
            mPositions = new Integer[tempPosition.size()];
            if (mSections.length > 0) {
                mSideBar.setCharArr(tempSections);
                mSideBar.setVisibility(View.VISIBLE);
                mSideBar.postInvalidate();
            }
            tempSections.toArray(mSections);
            tempPosition.toArray(mPositions);
            tempSections.clear();
            tempPosition.clear();
            tempPosition = null;
            tempSections = null;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Country getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void reflashData(List<Country> data) {
            if (data != null) {
                list = data;
            }
        }

        public void addObject(Country c) {
            if (c != null) {
                list.add(c);
            }
        }

        public List<Country> getList() {
            return list;
        }

        public void setList(List<Country> list) {
            this.list = list;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Country c = list.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.country_list_items, null);
                viewHolder = new ViewHolder();
                viewHolder.textSort = (TextView) convertView.findViewById(R.id.country_sort);
                viewHolder.textName = (TextView) convertView.findViewById(R.id.country_text);
                viewHolder.textCode = (TextView) convertView.findViewById(R.id.country_code);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            char ch = searchFilter.getAlpha(c.getCountry().toUpperCase());
            if (position == 0) {
                viewHolder.textSort.setVisibility(View.VISIBLE);
                viewHolder.textSort.setText(ch + "");
            } else {
                char lastCatalog = searchFilter.getAlpha(list.get(position - 1).getCountry().toUpperCase());
                if (ch == lastCatalog) {
                    viewHolder.textSort.setVisibility(View.GONE);
                } else {
                    viewHolder.textSort.setVisibility(View.VISIBLE);
                    viewHolder.textSort.setText(ch + "");
                }
            }
            viewHolder.textName.setText(c.getCountry());
            viewHolder.textCode.setText(c.getCode());
            return convertView;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getPositionForSection(int section) {
            if (section == '!') {
                return 0;
            } else {
                Country c = null;
                for (int i = 0; i < getCount(); i++) {
                    c = list.get(i);
                    char firstChar = searchFilter.getAlpha(c.getCountry().toUpperCase());
                    if (firstChar == section) {
                        return i;
                    }

                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            for (int i = 0; i < mPositions.length; i++) {
                if (position <= mPositions[i]) {
                    return i;
                }
            }
//			int index = Arrays.binarySearch(mPositions, position);
//			return index >= 0 ? index : 0;
            return 0;
        }

        final class ViewHolder {
            TextView textSort;
            TextView textName;
            TextView textCode;
        }
    }

    private class SearchTextWatcher implements TextWatcher {

        private List<Country> mmList = null;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mAdapter != null && s.length() == 0) {
                mmList = mAdapter.getList();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (text == null || text.length() == 0 && mAdapter != null) {
                mAdapter.reflashData(mmList);
                mAdapter.notifyDataSetChanged();
                return;
            }
            search(text);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDialogText);
    }

    private void getData() {
        countries = DBState.getInstance(this).getCountryList_toArrayList();
        mAdapter = new CountryAdapter(this, countries);
        mSideBar.setSectionIndexter(mAdapter);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_layout:
            case R.id.back_button:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mSideBar.setOrientation(newConfig.orientation);
        mSideBar.postInvalidate();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }

}
