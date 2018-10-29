package com.pchmn.materialchips.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pchmn.EmailValidator;
import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.R;
import com.pchmn.materialchips.model.ChipInterface;
import com.pchmn.materialchips.util.ColorUtil;
import com.pchmn.materialchips.views.ChipsInputEditText;
import com.pchmn.materialchips.views.DetailedChipView;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.util.ViewUtil;
import com.pchmn.materialchips.views.FilterableListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ChipsAdapter.class.toString();
    private static final int TYPE_EDIT_TEXT = 0; //EditTextType 0
    private static final int TYPE_ITEM = 1; //item 1
    private Context mContext; //context
    private ChipsInput mChipsInput; //chipsInput
    public static List<ChipInterface> mChipList = new ArrayList<>(); //chipList
    private String mHintLabel;
    private ChipsInputEditText mEditText;
    private RecyclerView mRecycler;


    public ChipsAdapter(Context context, ChipsInput chipsInput, RecyclerView recycler) {
        mContext = context;
        mChipsInput = chipsInput;
        mRecycler = recycler;
        mHintLabel = mChipsInput.getHint();
        mEditText = mChipsInput.getEditText();
        initEditText();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final ChipView chipView;

        ItemViewHolder(View view) {
            super(view);
            chipView = (ChipView) view;
        }
    }

    private class EditTextViewHolder extends RecyclerView.ViewHolder {

        private final EditText editText;

        EditTextViewHolder(View view) {
            super(view);
            editText = (EditText) view;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_EDIT_TEXT)
            return new EditTextViewHolder(mEditText);
        else
            return new ItemViewHolder(mChipsInput.getChipView());

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        // edit text
        if (position == mChipList.size()) {
            if (mChipList.size() == 0)
                mEditText.setHint(mHintLabel);

            // auto fit edit text
            autofitEditText();
            mEditText.requestFocus();
        }
        // chip
        else if (getItemCount() > 1) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.chipView.inflate(getItem(position));



            // handle click
            handleClickOnEditText(itemViewHolder.chipView, position);

        }
    }


    @Override
    public int getItemCount() {
        return mChipList.size() + 1;
    }

    private ChipInterface getItem(int position) {
        return mChipList.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mChipList.size())
            return TYPE_EDIT_TEXT;

        return TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return mChipList.get(position).hashCode();
    }

    private void initEditText() {
        mEditText.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mEditText.setHint(mHintLabel);
        mEditText.setBackgroundResource(android.R.color.transparent);
        // prevent fullscreen on landscape
        //mEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mEditText.setPrivateImeOptions("nm");
        // no suggestion
        mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mEditText.requestFocus();

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    Log.v("hihi","hihi");
                    String label = mEditText.getText().toString().trim();
                    String info = label;
                    Chip eChip = new Chip(label, info);
                    mChipList.add(eChip);
                    addChip(mChipList.get(mChipList.size() - 1));
                    notifyDataSetChanged();
                    mEditText.setText(""); //clear
                    return true;
                } else {
                    return false;
                }
            }
        });

        // handle back space
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // backspace
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    // remove last chip
                    if (mChipList.size() > 0 && mEditText.getText().toString().length() == 0)
                        removeChip(mChipList.size() - 1);
                }


                return false;
            }
        });


        // text changed
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mChipsInput.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void autofitEditText() {
        // min width of edit text = 50 dp
        ViewGroup.LayoutParams params = mEditText.getLayoutParams();
        params.width = ViewUtil.dpToPx(50);
        mEditText.setLayoutParams(params);

        // listen to change in the tree
        mEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // get right of recycler and left of edit text
                int right = mRecycler.getRight();
                int left = mEditText.getLeft();

                // edit text will fill the space
                ViewGroup.LayoutParams params = mEditText.getLayoutParams();
                params.width = right - left - ViewUtil.dpToPx(8);
                mEditText.setLayoutParams(params);

                // request focus
                mEditText.requestFocus();

                // remove the listener:
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mEditText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mEditText.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }

        });
    }

    private void handleClickOnEditText(ChipView chipView, final int position) {
        // delete chip
        chipView.setOnDeleteClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeChip(position);
            }
        });

        // show detailed chip
        if (mChipsInput.isShowChipDetailed()) {
            chipView.setOnChipClicked(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get chip position
                    int[] coord = new int[2];
                    v.getLocationInWindow(coord);
                    Log.v("coord0", String.valueOf(coord[0])); //x좌표
                    Log.v("coord1", String.valueOf(coord[1])); //y좌표

                    final DetailedChipView detailedChipView = mChipsInput.getDetailedChipView(getItem(position));
                    detailedChipView.currentChip(chipView); //pass chipView to detailChipView
                    setDetailedChipViewPosition(detailedChipView, coord);

                    // delete button
                    detailedChipView.setOnDeleteClicked(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeChip(position);
                            detailedChipView.fadeOut();
                        }
                    });


                }
            });


        }
    }


    private void setDetailedChipViewPosition(DetailedChipView detailedChipView, int[] coord) {
        // window width
        ViewGroup rootView = (ViewGroup) mRecycler.getRootView();
        int windowWidth = ViewUtil.getWindowWidth(mContext);

        //LayoutParams는 뷰가 소속되는 부모, 즉 레이아웃에 소속되는 속성
        //ex) layout_width, height같은 것
        //여기서는 RelativeLayout이 DetailedChipView의 부모이기 때문에 RelativeLayout.LayoutParams

        //textView는 setText()를 제공하지만 여기서는 속성을 바꾸는 것은 제공하지 않음
        //어느 레이아웃에 놓일지 모르기 때문이다.

        //getLayoutParams() 현재 설정되어있는 레이아웃 파라미터 조사
        //setLayoutParams() 새로운 파라미터로 조정



        // chip size
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewUtil.dpToPx(350),
                ViewUtil.dpToPx(80));
        layoutParams.topMargin = coord[1] + ViewUtil.dpToPx(40);
        layoutParams.leftMargin = ViewUtil.dpToPx(30);

//        //   align left window
//        if (coord[0] <= 0) {
//            layoutParams.leftMargin = 0;
//            layoutParams.topMargin = coord[1] - ViewUtil.dpToPx(13);
//            detailedChipView.alignLeft();
//        }
//        // align right
//        else if (coord[0] + ViewUtil.dpToPx(300) > windowWidth + ViewUtil.dpToPx(13)) {
//            layoutParams.leftMargin = windowWidth - ViewUtil.dpToPx(300);
//            layoutParams.topMargin = coord[1] - ViewUtil.dpToPx(13);
//            detailedChipView.alignRight();
//        }
//        // same position as chip
//        else {
//            layoutParams.leftMargin = coord[0] - ViewUtil.dpToPx(13);
//            layoutParams.topMargin = coord[1] - ViewUtil.dpToPx(0);
//        }
        // show view
        rootView.addView(detailedChipView, layoutParams);
        detailedChipView.fadeIn();
    }

    public void setFilterableListView(FilterableListView filterableListView) {
        if (mEditText != null)
            mEditText.setFilterableListView(filterableListView);
    }

    public void addChip(ChipInterface chip) {
        if (!listContains(mChipList, chip)) {
            mChipList.add(chip);
            // notify listener
            mChipsInput.onChipAdded(chip, mChipList.size());
            // hide hint
            mEditText.setHint(null);
            // reset text
            mEditText.setText(null);
            // refresh data
            notifyItemInserted(mChipList.size());
        }
    }

    public void removeChip(ChipInterface chip) {
        int position = mChipList.indexOf(chip);
        mChipList.remove(position);
        // notify listener
        notifyItemRangeChanged(position, getItemCount());
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChip(int position) {
        ChipInterface chip = mChipList.get(position);
        // remove contact
        mChipList.remove(position);
        // notify listener
        mChipsInput.onChipRemoved(chip, mChipList.size());
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipById(Object id) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getInfo() != null && chip.getInfo().equals(id)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipByLabel(String label) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getLabel().equals(label)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public void removeChipByInfo(String info) {
        for (Iterator<ChipInterface> iter = mChipList.listIterator(); iter.hasNext(); ) {
            ChipInterface chip = iter.next();
            if (chip.getInfo() != null && chip.getInfo().equals(info)) {
                // remove chip
                iter.remove();
                // notify listener
                mChipsInput.onChipRemoved(chip, mChipList.size());
            }
        }
        // if 0 chip
        if (mChipList.size() == 0)
            mEditText.setHint(mHintLabel);
        // refresh data
        notifyDataSetChanged();
    }

    public List<ChipInterface> getChipList() {
        return mChipList;
    }

    private boolean listContains(List<ChipInterface> contactList, ChipInterface chip) {

        if (mChipsInput.getChipValidator() != null) {
            for (ChipInterface item : contactList) {
                if (mChipsInput.getChipValidator().areEquals(item, chip))
                    return true;
            }
        } else {
            for (ChipInterface item : contactList) {
                if (chip.getInfo() != null && chip.getInfo().equals(item.getInfo()))
                    return true;
                if (chip.getInfo().equals(item.getLabel()))
                    return true;
            }
        }

        return false;
    }
}
